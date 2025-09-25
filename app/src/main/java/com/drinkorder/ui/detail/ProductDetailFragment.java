package com.drinkorder.ui.detail;
import android.os.Bundle;
import android.view.*;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import com.drinkorder.R;
public class ProductDetailFragment extends Fragment {
  @Nullable @Override public View onCreateView(@NonNull LayoutInflater i, @Nullable ViewGroup c, @Nullable Bundle b){
    return i.inflate(R.layout.fragment_product_detail, c, false);
  }
}
