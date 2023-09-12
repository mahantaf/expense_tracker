package com.example.expensestracker.transactions.activities;

import static androidx.core.view.ViewCompat.onInitializeAccessibilityNodeInfo;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.AccessibilityDelegateCompat;
import androidx.core.view.ViewCompat;
import androidx.core.view.accessibility.AccessibilityNodeInfoCompat;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.expensestracker.CustomButton;
import com.example.expensestracker.R;
import com.example.expensestracker.budget_categories.BudgetCategory;
import com.example.expensestracker.budget_categories.BudgetCategoryManager;
import com.example.expensestracker.transactions.Transaction;
import com.example.expensestracker.transactions.TransactionManager;
import com.example.expensestracker.usermoney.UserWallet;
import com.google.android.material.textfield.TextInputLayout;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

public class AddingTransactionActivity extends AppCompatActivity {

    Spinner productCategorySpinner;
    EditText productNameTextField;
    EditText productPriceTextField;
    DatePicker transactionDatePicker;

    ArrayList<String> productCategories;
    ArrayAdapter categoriesAdapter;
    private String tag;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_transaction);

        try {
            productCategorySpinner = findViewById(R.id.product_category_spinner);
            productNameTextField = findViewById(R.id.product_name_textField);
            productPriceTextField = findViewById(R.id.product_price_textField);
            transactionDatePicker = findViewById(R.id.date_picker);


            productCategories = BudgetCategoryManager.getCategoryNames();

            categoriesAdapter = new ArrayAdapter(this, android.R.layout.simple_dropdown_item_1line, productCategories);
            productCategorySpinner.setAdapter(categoriesAdapter);
            productCategorySpinner.setSelection(0);


            /*
             * ==================================================
             */


        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void onClickAddTransaction(View view)
    {
        try {
            if(UserWallet.getUserMoney() > 0) {

                Date transactionDate = getDateFromDatePicker();
                Date currentDate = Calendar.getInstance().getTime();

                // TODO : dont allow future transactions

                if (productCategorySpinner.getAdapter() != null && productCategorySpinner.getAdapter().getCount() == 0) {
                    Toast.makeText(getApplicationContext() , "Please create budget categories first", Toast.LENGTH_LONG).show();
                } else {
                    BudgetCategory budgetCategory = BudgetCategoryManager.
                            getBudgetCategoryByName(productCategorySpinner.getSelectedItem().toString());

                    String productName = productNameTextField.getText().toString();
                    double productPrice = Double.parseDouble(productPriceTextField.getText().toString());

                    if (infoMissing(productName, productPrice)){
                        Toast.makeText(getApplicationContext() , "Please fill up all necessary information", Toast.LENGTH_LONG).show();
                    }
                    else {
                        Transaction transaction = new Transaction(budgetCategory.getID(), productName, productPrice, transactionDate);
                        TransactionManager.addNewTransaction(transaction);

                        makeLongToast(transaction.toString());

                        UserWallet.takeFromWallet(productPrice);
                    }
                }
            }
            else
            {
                Toast.makeText(getApplicationContext() , "Your Wallet is empty, " +
                        "You can not buy anything for now",Toast.LENGTH_LONG).show();
            }
            finish();
        }
        catch (Exception e)
        {
            makeLongToast(e.getMessage());
            e.printStackTrace();
        }

    }

    private boolean infoMissing(String productName, double productPrice) {
        Log.i("TAG", productName + " x $" + String.valueOf(productPrice));
        return false;
    }

    private Date getDateFromDatePicker() {
        int day = transactionDatePicker.getDayOfMonth();
        int month = transactionDatePicker.getMonth();
        int year = transactionDatePicker.getYear();

        Calendar calendar = Calendar.getInstance();
        calendar.set(year,month,day);

        return calendar.getTime();
    }

    private void makeLongToast(String msg)
    {
        Toast.makeText(getApplicationContext(),msg , Toast.LENGTH_LONG).show();
    }
}