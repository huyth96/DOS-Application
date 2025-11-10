package com.drinkorder.ui.admin;

import android.app.Dialog;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.DialogFragment;

import com.drinkorder.R;
import com.drinkorder.data.db.entity.CategoryEntity;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.android.material.textfield.TextInputEditText;

public class AdminCategoryDialog extends DialogFragment {

  private static final String ARG_IS_EDIT = "arg_is_edit";
  private static final String ARG_NAME = "arg_name";
  private static final String ARG_DESC = "arg_desc";

  private TextInputEditText edtName;
  private TextInputEditText edtDescription;
  private Listener listener;

  public interface Listener {
    void onSubmit(String name, String description);
  }

  public static AdminCategoryDialog newInstance(@Nullable CategoryEntity category) {
    AdminCategoryDialog dialog = new AdminCategoryDialog();
    Bundle args = new Bundle();
    if (category != null) {
      args.putBoolean(ARG_IS_EDIT, true);
      args.putString(ARG_NAME, category.name);
      args.putString(ARG_DESC, category.description);
    }
    dialog.setArguments(args);
    return dialog;
  }

  public void setListener(Listener listener) {
    this.listener = listener;
  }

  @NonNull
  @Override
  public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
    View view = LayoutInflater.from(requireContext()).inflate(R.layout.dialog_admin_category, null, false);
    edtName = view.findViewById(R.id.edtCategoryName);
    edtDescription = view.findViewById(R.id.edtCategoryDescription);

    Bundle args = getArguments();
    boolean isEdit = args != null && args.getBoolean(ARG_IS_EDIT, false);
    if (args != null) {
      edtName.setText(args.getString(ARG_NAME, ""));
      edtDescription.setText(args.getString(ARG_DESC, ""));
    }

    AlertDialog dialog = new MaterialAlertDialogBuilder(requireContext())
        .setTitle(isEdit ? "Edit category" : "Add category")
        .setView(view)
        .setNegativeButton("Cancel", (d, which) -> dismiss())
        .setPositiveButton("Save", null)
        .create();

    dialog.setOnShowListener(d -> {
      dialog.getButton(AlertDialog.BUTTON_POSITIVE).setOnClickListener(v -> {
        String name = textOf(edtName);
        if (TextUtils.isEmpty(name)) {
          edtName.setError("Name cannot be empty");
          return;
        }
        String description = textOf(edtDescription);
        if (listener != null) listener.onSubmit(name.trim(), description.trim());
        dismiss();
      });
    });

    return dialog;
  }

  private String textOf(TextInputEditText edt) {
    return edt.getText() == null ? "" : edt.getText().toString().trim();
  }
}
