package com.drinkorder.ui.admin;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.drinkorder.R;
import com.drinkorder.data.db.entity.CategoryEntity;
import com.drinkorder.databinding.FragmentAdminCategoriesBinding;
import com.drinkorder.ui.MainActivity;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class AdminCategoriesFragment extends Fragment {
    private FragmentAdminCategoriesBinding binding;
    private AdminCategoryVM viewModel;
    private AdminCategoriesAdapter adapter;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentAdminCategoriesBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        viewModel = new ViewModelProvider(this).get(AdminCategoryVM.class);

        // Setup RecyclerView
        adapter = new AdminCategoriesAdapter(viewModel);
        binding.rvCategories.setLayoutManager(new LinearLayoutManager(getContext()));
        binding.rvCategories.setAdapter(adapter);

        viewModel.getAllCategories().observe(getViewLifecycleOwner(), categories -> {
            adapter.submitList(categories); // Bây giờ submitList hoạt động
        });

        // Thêm category
        binding.btnAddCategory.setOnClickListener(v -> {
            String name = binding.etCategoryName.getText().toString().trim();
            String desc = binding.etCategoryDescription.getText().toString().trim();
            if (!name.isEmpty()) {
                CategoryEntity category = new CategoryEntity();
                category.name = name;
                category.description = desc;
                category.createdAt = System.currentTimeMillis();
                viewModel.insert(category);
                Toast.makeText(getContext(), "Thêm thành công", Toast.LENGTH_SHORT).show();
                binding.etCategoryName.setText("");
                binding.etCategoryDescription.setText("");
            }
        });
        // Trong onViewCreated
        binding.btnBack.setOnClickListener(v -> {
            // Lấy BottomNavigationView từ MainActivity
            if (getActivity() instanceof MainActivity) {
                BottomNavigationView nav = getActivity().findViewById(R.id.bottomNav);
                if (nav != null) {
                    nav.setSelectedItemId(R.id.tab_admin_products);  // Quay về tab Products (hoặc tab gần nhất bạn muốn)
                }
            }
            // Hoặc nếu muốn back thực sự, thêm addToBackStack() khi replace fragment ở MainActivity
        });
    }
}