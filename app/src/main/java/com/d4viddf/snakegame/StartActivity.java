package com.d4viddf.snakegame;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.games.Games;
import com.google.android.gms.games.GamesClient;
import com.google.android.gms.games.PlayersClient;
import com.google.android.gms.games.stats.PlayerStats;
import com.google.android.gms.tasks.Task;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.card.MaterialCardView;

import java.util.Objects;

public class StartActivity extends AppCompatActivity {
    private MaterialButton login;
    private MaterialCardView card;
    private TextView username;
    private boolean permiso = true;
    private PrefManager prefManager;
    private static final int RC_SIGN_IN = 9001;
    private GoogleSignInOptions signInOptions;
    private PlayersClient mPlayersClient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start);
        prefManager = new PrefManager(this);
        signInOptions =
                new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_GAMES_SIGN_IN)
                        .requestScopes(Games.SCOPE_GAMES_SNAPSHOTS)
                        .build();
        MaterialButton play = findViewById(R.id.play);
        play.setOnClickListener(v -> {
            new Utils().vibrateButtonApple(getApplicationContext());
            Intent intent = new Intent(StartActivity.this, Controls.class);
            startActivity(intent);
        });
        login = findViewById(R.id.login);

        login.setText(mPlayersClient != null ? R.string.logout : R.string.login);
        login.setOnClickListener(v -> {
            new Utils().vibrateButtonApple(getApplicationContext());
            if (mPlayersClient != null) {
                onDisconnected();
                login.setText(R.string.login);
            } else {
                startSignInIntent();
                login.setText(R.string.logout);
            }
        });
        card = findViewById(R.id.ach);
        if (prefManager.isUserChild()) {
            login.setVisibility(View.GONE);
            card.setVisibility(View.GONE);
            onDisconnected();
        }
        if (!prefManager.isUserChild() && mPlayersClient == null) card.setVisibility(View.GONE);
        username = findViewById(R.id.username);
        card.setOnClickListener(v -> {
            new Utils().vibrateButtonApple(getApplicationContext());
            showAchievements();
        });
    }


    private void startSignInIntent() {
        GoogleSignInClient signInClient = GoogleSignIn.getClient(this,
                GoogleSignInOptions.DEFAULT_GAMES_SIGN_IN);
        Intent intent = signInClient.getSignInIntent();
        startActivityForResult(intent, RC_SIGN_IN);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == RC_SIGN_IN) {
            Task<GoogleSignInAccount> task =
                    GoogleSignIn.getSignedInAccountFromIntent(data);
            try {
                GoogleSignInAccount account = task.getResult(ApiException.class);
                Log.i("Login", "login");
                onConnected(account);
                permiso = true;
            } catch (ApiException apiException) {
                Log.i("login", apiException.getMessage());
                onDisconnected();
                permiso = false;
            }

        }
    }

    private void onDisconnected() {
        card.setVisibility(View.GONE);
        GoogleSignIn.getClient(this, signInOptions).signOut();
        login.setText(R.string.login);
        mPlayersClient = null;
    }

    private void onConnected(GoogleSignInAccount account) {
        GamesClient gamesClient = Games.getGamesClient(this, Objects.requireNonNull(GoogleSignIn.getLastSignedInAccount(this)));
        gamesClient.setViewForPopups(findViewById(android.R.id.content));
        gamesClient.setGravityForPopups(Gravity.TOP | Gravity.CENTER_HORIZONTAL);

        mPlayersClient = Games.getPlayersClient(this, account);
        mPlayersClient.getCurrentPlayer()
                .addOnCompleteListener(task -> {
                    String displayName;
                    if (task.isSuccessful()) {
                        displayName = task.getResult().getDisplayName();
                        username.setText(displayName);
                    } else {
                        Exception e = task.getException();
                        if (e != null) {
                            e.printStackTrace();
                        }
                    }
                });
        checkPlayerStats();
        Log.i("Login", mPlayersClient.getCurrentPlayer().toString());
        login.setText(mPlayersClient != null ? R.string.logout : R.string.login);
        card.setVisibility(View.VISIBLE);
    }

    private static final int RC_ACHIEVEMENT_UI = 9003;

    private void showAchievements() {
        Games.getAchievementsClient(this, Objects.requireNonNull(GoogleSignIn.getLastSignedInAccount(this)))
                .getAchievementsIntent()
                .addOnSuccessListener(intent -> startActivityForResult(intent, RC_ACHIEVEMENT_UI));
    }

    public void checkPlayerStats() {
        Games.getPlayerStatsClient(this, Objects.requireNonNull(GoogleSignIn.getLastSignedInAccount(this)))
                .loadPlayerStats(true)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        if (task.getResult().isStale()) Log.d("TAG", "using cached data");
                        PlayerStats stats = task.getResult().get();
                        if (stats != null) {
                            if (stats.getNumberOfSessions() > 1000) {
                                Games.getAchievementsClient(this, Objects.requireNonNull(GoogleSignIn.getLastSignedInAccount(this)))
                                        .unlock(String.valueOf(R.string.achievement_mobile_addict));
                                GamesClient gamesClient = Games.getGamesClient(this, Objects.requireNonNull(GoogleSignIn.getLastSignedInAccount(this)));
                                gamesClient.setViewForPopups(findViewById(android.R.id.content));
                                gamesClient.setGravityForPopups(Gravity.TOP | Gravity.CENTER_HORIZONTAL);
                            }
                        }
                    }
                });
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (!prefManager.isUserChild() && mPlayersClient == null && permiso) startSignInIntent();
    }
}