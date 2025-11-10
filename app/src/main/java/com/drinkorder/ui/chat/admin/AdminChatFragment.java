package com.drinkorder.ui.chat.admin;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.drinkorder.R;
import com.drinkorder.data.model.ChatThreadSummary;

import java.util.List;

public class AdminChatFragment extends Fragment {

  private AdminChatViewModel viewModel;
  private AdminChatThreadAdapter adapter;
  private RecyclerView recyclerView;
  private View emptyState;
  private TextView tvEmptyTitle;
  private TextView tvEmptySubtitle;
  private TextView tvConnection;
  private View detailContainer;
  private boolean tabletMode;

  @Nullable
  @Override
  public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                           @Nullable Bundle savedInstanceState) {
    return inflater.inflate(R.layout.fragment_admin_chat, container, false);
  }

  @Override
  public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
    super.onViewCreated(view, savedInstanceState);
    recyclerView = view.findViewById(R.id.rvAdminChatThreads);
    emptyState = view.findViewById(R.id.chatEmptyState);
    tvEmptyTitle = view.findViewById(R.id.tvChatEmptyTitle);
    tvEmptySubtitle = view.findViewById(R.id.tvChatEmptySubtitle);
    tvConnection = view.findViewById(R.id.tvChatConnection);
    detailContainer = view.findViewById(R.id.chatDetailContainer);
    tabletMode = detailContainer != null;

    recyclerView.setLayoutManager(new LinearLayoutManager(requireContext()));
    adapter = new AdminChatThreadAdapter(this::openThread);
    recyclerView.setAdapter(adapter);

    viewModel = new ViewModelProvider(requireActivity()).get(AdminChatViewModel.class);

    viewModel.getThreads().observe(getViewLifecycleOwner(), this::renderThreads);
    viewModel.getSelectedThreadId().observe(getViewLifecycleOwner(), threadId -> {
      adapter.setSelectedThreadId(threadId);
      if (tabletMode && threadId != null) {
        showDetail(threadId);
      }
    });
    viewModel.getConnectionState().observe(getViewLifecycleOwner(), connected -> {
      if (tvConnection == null) return;
      boolean show = connected != null && !connected;
      tvConnection.setVisibility(show ? View.VISIBLE : View.GONE);
    });
  }

  private void renderThreads(List<ChatThreadSummary> summaries) {
    adapter.submit(summaries);
    boolean empty = summaries == null || summaries.isEmpty();
    if (emptyState != null) {
      emptyState.setVisibility(empty ? View.VISIBLE : View.GONE);
    }
    if (recyclerView != null) {
      recyclerView.setVisibility(empty ? View.GONE : View.VISIBLE);
    }
    if (tabletMode && !empty) {
      String current = viewModel.getSelectedThreadId().getValue();
      if (current == null && summaries != null && !summaries.isEmpty()) {
        viewModel.selectThread(summaries.get(0).getThreadId());
      }
    }
  }

  private void openThread(ChatThreadSummary summary) {
    if (summary == null) return;
    viewModel.selectThread(summary.getThreadId());
    if (tabletMode) {
      showDetail(summary.getThreadId());
    } else {
      Fragment fragment = AdminChatDetailFragment.newInstance(summary.getThreadId());
      requireActivity().getSupportFragmentManager()
          .beginTransaction()
          .replace(R.id.container, fragment)
          .addToBackStack("adminChatDetail")
          .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
          .commit();
    }
  }

  private void showDetail(String threadId) {
    if (!tabletMode || detailContainer == null) return;
    Fragment existing = getChildFragmentManager().findFragmentByTag("adminChatDetail");
    if (existing instanceof AdminChatDetailFragment) {
      AdminChatDetailFragment detail = (AdminChatDetailFragment) existing;
      if (threadId.equals(detail.getThreadId())) return;
    }
    Fragment fragment = AdminChatDetailFragment.newInstance(threadId);
    getChildFragmentManager()
        .beginTransaction()
        .replace(R.id.chatDetailContainer, fragment, "adminChatDetail")
        .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE)
        .commit();
  }
}
