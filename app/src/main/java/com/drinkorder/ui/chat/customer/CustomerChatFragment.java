package com.drinkorder.ui.chat.customer;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.drinkorder.R;
import com.drinkorder.databinding.FragmentCustomerChatBinding;
import com.google.android.material.banner.MaterialBanner;
import com.google.android.material.snackbar.Snackbar;

import java.util.List;

public class CustomerChatFragment extends Fragment {

  private FragmentCustomerChatBinding binding;
  private CustomerChatVM viewModel;
  private CustomerChatMessageAdapter adapter;
  private Snackbar sendingSnackbar;

  @Nullable
  @Override
  public View onCreateView(
      @NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
    binding = FragmentCustomerChatBinding.inflate(inflater, container, false);
    return binding.getRoot();
  }

  @Override
  public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
    super.onViewCreated(view, savedInstanceState);
    viewModel = new ViewModelProvider(requireActivity()).get(CustomerChatVM.class);

    adapter = new CustomerChatMessageAdapter();
    LinearLayoutManager layoutManager = new LinearLayoutManager(requireContext());
    layoutManager.setStackFromEnd(true);
    binding.rvMessages.setLayoutManager(layoutManager);
    binding.rvMessages.setAdapter(adapter);

    binding.btnSend.setOnClickListener(v -> {
      CharSequence content = binding.edtMessage.getText();
      String message = content == null ? null : content.toString().trim();
      boolean accepted = viewModel.sendMessage(message);
      if (accepted) {
        binding.edtMessage.setText("");
      }
    });

    viewModel.getMessages().observe(getViewLifecycleOwner(), this::renderMessages);

    viewModel.getSendState().observe(getViewLifecycleOwner(), state -> {
      if (state == null) return;
      if (state == CustomerChatVM.SendState.SENDING) {
        showSendingSnackbar();
      } else {
        dismissSendingSnackbar();
        if (state == CustomerChatVM.SendState.ERROR) {
          viewModel.resetSendState();
        }
      }
    });

    viewModel.getErrorMessage().observe(getViewLifecycleOwner(), message -> {
      if (TextUtils.isEmpty(message)) return;
      Snackbar.make(binding.getRoot(), message, Snackbar.LENGTH_LONG).show();
      viewModel.clearErrorMessage();
    });

    viewModel.getConnectionState().observe(getViewLifecycleOwner(), this::updateBanner);
    viewModel.getConnectionErrorMessage().observe(getViewLifecycleOwner(), msg -> {
      CustomerChatVM.ConnectionState state = viewModel.getConnectionState().getValue();
      if (state == CustomerChatVM.ConnectionState.ERROR) {
        updateBanner(state);
      }
    });
  }

  @Override
  public void onResume() {
    super.onResume();
    if (viewModel != null) {
      viewModel.markThreadAsRead();
    }
  }

  @Override
  public void onDestroyView() {
    super.onDestroyView();
    dismissSendingSnackbar();
    binding = null;
  }

  private void renderMessages(List<CustomerChatMessageItem> items) {
    adapter.submitList(items);
    if (items == null || items.isEmpty()) return;
    binding.rvMessages.post(() -> binding.rvMessages.scrollToPosition(items.size() - 1));
  }

  private void showSendingSnackbar() {
    if (sendingSnackbar != null && sendingSnackbar.isShown()) return;
    sendingSnackbar =
        Snackbar.make(binding.getRoot(), R.string.chat_send_in_progress, Snackbar.LENGTH_INDEFINITE);
    sendingSnackbar.show();
  }

  private void dismissSendingSnackbar() {
    if (sendingSnackbar != null) {
      sendingSnackbar.dismiss();
      sendingSnackbar = null;
    }
  }

  private void updateBanner(CustomerChatVM.ConnectionState state) {
    if (binding == null) return;
    MaterialBanner banner = binding.bannerStatus;
    if (state == null) {
      banner.setVisibility(View.GONE);
      banner.setActionTextOnClickListener(null, null);
      return;
    }
    if (state == CustomerChatVM.ConnectionState.CONNECTING) {
      banner.setVisibility(View.VISIBLE);
      banner.setPrimaryText(getString(R.string.chat_connecting));
      banner.setSecondaryText(getString(R.string.chat_connecting_subtitle));
      banner.setActionTextOnClickListener(null, null);
    } else if (state == CustomerChatVM.ConnectionState.ERROR) {
      banner.setVisibility(View.VISIBLE);
      banner.setPrimaryText(getString(R.string.chat_connection_failed));
      String secondary = viewModel.getConnectionErrorMessage().getValue();
      if (TextUtils.isEmpty(secondary)) {
        secondary = getString(R.string.chat_banner_error_subtitle);
      }
      banner.setSecondaryText(secondary);
      banner.setActionTextOnClickListener(
          getString(R.string.chat_banner_retry), v -> viewModel.retryConnection());
    } else {
      banner.setVisibility(View.GONE);
      banner.setActionTextOnClickListener(null, null);
    }
  }
}
