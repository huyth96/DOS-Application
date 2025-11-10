#!/usr/bin/env node
/**
 * Minimal WebSocket chat backend used for local development.
 *
 * Usage:
 *   npm install ws
 *   node scripts/chat-dev-server.js
 *
 * You can customize host/port/path via env variables:
 *   CHAT_SERVER_HOST=0.0.0.0 CHAT_SERVER_PORT=8080 CHAT_SERVER_PATH=/ws node scripts/chat-dev-server.js
 */

const { WebSocketServer } = require('ws');
const { URL } = require('node:url');

const HOST = process.env.CHAT_SERVER_HOST || '0.0.0.0';
const PORT = Number(process.env.CHAT_SERVER_PORT || 8080);
const PATH = process.env.CHAT_SERVER_PATH || '/ws';

const threads = new Map();
const threadMessages = new Map();
const clients = new Map(); // socket -> meta

function now() {
  return Date.now();
}

function timeAgo(minutes) {
  return now() - minutes * 60_000;
}

function seedDemoData() {
  createDemoThread('demo-alice', {
    title: 'Alice Nguyen',
    userId: 101,
    messages: [
      {
        senderRole: 'customer',
        body: 'Em cần chỉnh đơn hàng #1032, giúp em nhé?',
        sentAt: timeAgo(45)
      },
      {
        senderRole: 'support',
        body: 'Chắc chắn rồi, em cần thay đổi điều gì?',
        sentAt: timeAgo(44)
      }
    ],
    unreadCount: 1
  });

  createDemoThread('demo-bob', {
    title: 'Bob Tran',
    userId: 102,
    messages: [
      {
        senderRole: 'customer',
        body: 'Quán còn cold brew không?',
        sentAt: timeAgo(10)
      }
    ],
    unreadCount: 1
  });
}

function createDemoThread(threadId, { title, userId, messages = [], unreadCount = 0 }) {
  const preparedMessages = messages.map((msg, index) => ({
    messageId: `${threadId}-msg-${index + 1}`,
    threadId,
    senderRole: msg.senderRole || 'customer',
    body: msg.body || '',
    sentAt: msg.sentAt || now(),
    deliveredAt: msg.sentAt || now(),
    isOutgoing: false
  }));
  const lastMessage = preparedMessages[preparedMessages.length - 1];
  threads.set(threadId, {
    threadId,
    userId,
    title: title || `Khách #${userId}`,
    lastMessage: lastMessage ? lastMessage.body : '',
    lastSenderRole: lastMessage ? lastMessage.senderRole : 'customer',
    lastTimestamp: lastMessage ? lastMessage.sentAt : now(),
    unreadCount,
    updatedAt: now()
  });
  threadMessages.set(threadId, preparedMessages);
}

seedDemoData();

const server = new WebSocketServer({ host: HOST, port: PORT, path: PATH }, () => {
  console.log(`Chat dev server listening on ws://${HOST}:${PORT}${PATH}`);
});

server.on('connection', (socket, request) => {
  const meta = parseClientMeta(request);
  clients.set(socket, meta);
  ensureThread(meta.threadId, meta);

  console.log(`Client connected: ${meta.username} (${meta.role}) -> ${meta.threadId}`);

  const visibleThreads = meta.role === 'admin' || meta.role === 'support'
    ? null
    : new Set([meta.threadId]);

  sendThreadList(socket, visibleThreads);
  sendExistingMessages(socket, visibleThreads);

  socket.on('message', raw => {
    try {
      const payload = JSON.parse(raw.toString());
      handleClientPayload(socket, payload);
    } catch (err) {
      console.error('Invalid payload received', err);
      socket.send(JSON.stringify({
        type: 'error',
        payload: { message: 'Invalid JSON payload' }
      }));
    }
  });

  socket.on('close', () => {
    clients.delete(socket);
    console.log(`Client disconnected: ${meta.username}`);
  });
});

function parseClientMeta(request) {
  try {
    const origin = request.headers.host ? `http://${request.headers.host}` : 'http://localhost';
    const fullUrl = new URL(request.url, origin);
    const userId = fullUrl.searchParams.get('userId') || `guest-${Math.floor(Math.random() * 10_000)}`;
    const username = fullUrl.searchParams.get('username') || `User ${userId}`;
    const role = (fullUrl.searchParams.get('role') || 'customer').toLowerCase();
    const threadId = fullUrl.searchParams.get('threadId') || `thread-${userId}`;
    return { userId, username, role, threadId };
  } catch (err) {
    console.warn('Failed to parse client meta', err);
    return {
      userId: `guest-${Math.floor(Math.random() * 10_000)}`,
      username: 'Guest',
      role: 'customer',
      threadId: 'demo-alice'
    };
  }
}

function sendThreadList(socket, allowedThreadIds) {
  const allowed = normalizeFilter(allowedThreadIds);
  const payload = {
    type: 'thread.list',
    payload: {
      threads: Array.from(threads.values())
        .filter(thread => !allowed || allowed.has(thread.threadId))
        .sort((a, b) => b.updatedAt - a.updatedAt)
    }
  };
  socket.send(JSON.stringify(payload));
}

function sendExistingMessages(socket, allowedThreadIds) {
  const allowed = normalizeFilter(allowedThreadIds);
  for (const [threadId, messages] of threadMessages.entries()) {
    if (allowed && !allowed.has(threadId)) continue;
    messages.forEach(msg => socket.send(JSON.stringify({ type: 'message.new', payload: msg })));
  }
}

function normalizeFilter(filter) {
  if (!filter) return null;
  if (filter instanceof Set) return filter;
  if (Array.isArray(filter)) {
    return new Set(filter);
  }
  return new Set([filter]);
}

function handleClientPayload(socket, payload) {
  if (!payload || typeof payload !== 'object') return;
  switch (payload.type) {
    case 'message.send':
      handleMessageSend(socket, payload);
      break;
    case 'thread.read':
      handleThreadRead(socket, payload);
      break;
    default:
      console.log('Unhandled payload type', payload.type);
  }
}

function ensureThread(threadId, meta) {
  if (!threadId) return;
  if (!threads.has(threadId)) {
    const title = meta && meta.role && meta.role !== 'admin' && meta.role !== 'support'
      ? meta.username || `Khách ${meta.userId}`
      : 'Barista Support';
    const numericUserId = meta && Number.isFinite(Number(meta.userId))
      ? Number(meta.userId)
      : 0;
    threads.set(threadId, {
      threadId,
      userId: numericUserId,
      title,
      lastMessage: '',
      lastSenderRole: '',
      lastTimestamp: now(),
      unreadCount: 0,
      updatedAt: now()
    });
  }
  if (!threadMessages.has(threadId)) {
    threadMessages.set(threadId, []);
  }
}

function handleMessageSend(socket, payload) {
  const meta = clients.get(socket);
  if (!meta) return;
  const clientMessageId = payload.clientMessageId || `local-${now()}`;
  const sanitizedBody = (payload.body || '').trim();
  if (!sanitizedBody) {
    socket.send(JSON.stringify({
      type: 'error',
      payload: { message: 'Message body is required' }
    }));
    return;
  }

  const targetThreadId = payload.threadId || meta.threadId;
  if (!targetThreadId) {
    socket.send(JSON.stringify({
      type: 'error',
      payload: { message: 'threadId is required' }
    }));
    return;
  }

  ensureThread(targetThreadId, meta);

  const senderRole = (payload.senderRole || meta.role || 'customer').toLowerCase();
  const sentAt = now();
  const messageId = `srv-${sentAt}-${Math.floor(Math.random() * 10_000)}`;
  const message = {
    messageId,
    threadId: targetThreadId,
    senderRole,
    body: sanitizedBody,
    sentAt,
    deliveredAt: sentAt,
    isOutgoing: false
  };

  threadMessages.get(targetThreadId).push(message);
  updateThreadState(targetThreadId, message);

  const ack = {
    type: 'message.ack',
    payload: {
      clientMessageId,
      messageId,
      deliveredAt: sentAt
    }
  };
  socket.send(JSON.stringify(ack));

  broadcastToThread(targetThreadId, {
    type: 'thread.updated',
    payload: threads.get(targetThreadId)
  });

  broadcastToThread(targetThreadId, {
    type: 'message.new',
    payload: message
  });
}

function updateThreadState(threadId, message) {
  const thread = threads.get(threadId);
  if (!thread) return;
  thread.lastMessage = message.body;
  thread.lastSenderRole = message.senderRole;
  thread.lastTimestamp = message.sentAt;
  thread.updatedAt = now();
  if (message.senderRole === 'customer') {
    thread.unreadCount += 1;
  } else {
    thread.unreadCount = 0;
  }
}

function broadcastToThread(threadId, payload) {
  const json = JSON.stringify(payload);
  for (const [socket, meta] of clients.entries()) {
    if (meta.role === 'admin' || meta.role === 'support' || meta.threadId === threadId) {
      try {
        socket.send(json);
      } catch (err) {
        console.warn('Failed to send payload', err);
      }
    }
  }
}

function handleThreadRead(socket, payload) {
  const threadId = payload.threadId;
  if (!threadId || !threads.has(threadId)) return;
  const thread = threads.get(threadId);
  thread.unreadCount = 0;
  thread.updatedAt = now();
  broadcastToThread(threadId, { type: 'thread.updated', payload: thread });
  const meta = clients.get(socket);
  if (meta) {
    console.log(`Thread ${threadId} marked as read by ${meta.username}`);
  }
}

process.on('SIGINT', () => {
  console.log('Shutting down chat dev server...');
  server.close(() => process.exit(0));
});
