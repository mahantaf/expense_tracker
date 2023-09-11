package com.example.expensestracker.mainactivity.fragments.budgetmanager;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;

import com.example.expensestracker.R;
import com.example.expensestracker.budget_categories.BudgetCategory;
import com.example.expensestracker.budget_categories.ui.BudgetCategoryInfoFragment;
import com.example.expensestracker.transactions.activities.ListingCategoryTransactionsActivity;

import java.util.ArrayList;

public class BudgetManagerListAdapter extends BaseAdapter
{
    ArrayList<BudgetCategory> budgetCategories;
    Context context;

    FragmentManager parentFragmentManager;

    public BudgetManagerListAdapter(Context context, ArrayList<BudgetCategory> budgetCategories, FragmentManager fragmentManager) {
        this.budgetCategories = budgetCategories;

        this.context = context;

        parentFragmentManager = fragmentManager;
    }

    @Override
    public int getCount() {
        return budgetCategories.size();
    }

    @Override
    public Object getItem(int i) {
        return budgetCategories.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup)
    {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService( Context.LAYOUT_INFLATER_SERVICE );
        view = inflater.inflate(R.layout.list_item_budget_manager, null);

        final BudgetCategory budgetCategory = budgetCategories.get(i);

        TextView categoryName = view.findViewById(R.id.category_name);
        TextView maxExpenses = view.findViewById(R.id.max_expenses);

        categoryName.setText(budgetCategory.getName());
        maxExpenses.setText("$"+String.valueOf(budgetCategory.getMaxBudget()));

        categoryName.setOnClickListener(v -> editBudgetCategory(budgetCategory));
        maxExpenses.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                viewCategoryTransactions(budgetCategory);
            }
        });

        return view;
    }

    private void viewCategoryTransactions(BudgetCategory budgetCategory) {
        try{
            Intent intent = new Intent( context , ListingCategoryTransactionsActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            intent.putExtra("category", budgetCategory.getName());
            context.startActivity(intent);
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    void editBudgetCategory( BudgetCategory budgetCategory)
    {

        BudgetCategoryInfoFragment budgetCategoryInfoFragment = new BudgetCategoryInfoFragment(budgetCategory);

        budgetCategoryInfoFragment.show(parentFragmentManager,"New budget");
    }
}
