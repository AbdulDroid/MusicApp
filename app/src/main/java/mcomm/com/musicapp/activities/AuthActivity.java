package mcomm.com.musicapp.activities;

import android.content.Intent;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Toast;

import mcomm.com.musicapp.R;
import mcomm.com.musicapp.fragments.LoginFragment;
import mcomm.com.musicapp.fragments.SignUpFragment;

public class AuthActivity extends AppCompatActivity implements
        LoginFragment.OnFragmentInteractionListener,
        SignUpFragment.OnFragmentInteractionListener{

    private LoginFragment loginFragment;
    private SignUpFragment signUpFragment;
    private Toast mToast;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_auth);
        showLoginFragment();
    }

    private void showLoginFragment(){
        if (loginFragment == null)
            loginFragment = LoginFragment.newInstance();
        loadFragment(loginFragment);
    }

    private void showSignUpFragment(){
        if (signUpFragment == null)
            signUpFragment = SignUpFragment.newInstance();
        loadFragment(signUpFragment);
    }

    @Override
    public void onBackPressed() {
        if (signUpFragment != null && signUpFragment.isAdded())
            showLoginFragment();
        else if (loginFragment != null && loginFragment.isAdded())
            this.finish();
    }

    void loadFragment(Fragment fragment){
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.fragmentContainer, fragment, fragment.getClass().getSimpleName())
                .addToBackStack(fragment.getClass().getSimpleName())
                .commit();
    }

    @Override
    public void onActionSignUp() {
        showSignUpFragment();
    }

    @Override
    public void onLogin(String userOrEmail, String password) {
        Intent intent = new Intent(this, CategoryActivity.class);
        startActivity(intent);
        this.finish();
    }

    @Override
    public void onActionLogin() {
        showLoginFragment();
    }

    @Override
    public void onSignUp(String username, String email, String password) {
        Intent intent = new Intent(this, CategoryActivity.class);
        startActivity(intent);
        this.finish();
    }
}
