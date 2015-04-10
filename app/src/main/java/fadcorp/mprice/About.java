package fadcorp.mprice;

import android.app.ActionBar;
import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.webkit.WebView;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * Created by fad on 08/11/14.
 */
public class About extends Activity {

    private TextView versionButton;
    private WebView webView;
    private ImageView share;
    private String market_uri;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTitle(R.string.about);
        setContentView(R.layout.about);

        setupUI();
    }

    protected void setupUI() {

        ActionBar actionBar = getActionBar();
        actionBar.hide();

        webView = (WebView) findViewById(R.id.webView);
        webView.getSettings().setJavaScriptEnabled(true);
        /* //Utilisation des variables
        webView.setWebViewClient(new WebViewClient() {
            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);
                view.loadUrl(String.format("javascript:replace('vesion', 'V %1$s' )", BuildConfig.VERSION_NAME));
            }
        }); */
        webView.loadUrl("file:///android_asset/about.html");

        versionButton = (TextView) findViewById(R.id.versionButton);
        versionButton.setText(String.format(
            getString(R.string.version_button_label),
            BuildConfig.VERSION_NAME));
        versionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                market_uri = getString(R.string.app_market_url);
                Intent intent = new Intent(Intent.ACTION_VIEW);
                intent.setData(Uri.parse(market_uri));
                startActivity(intent);
            }
        });

        share = (ImageView)findViewById(R.id.share);
        share.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent sharingIntent = new Intent(android.content.Intent.ACTION_SEND);
                        sharingIntent.setType("text/plain");
                        String shareBody = Constants.share;
                        sharingIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, R.string.app_name);
                        sharingIntent.putExtra(android.content.Intent.EXTRA_TEXT, shareBody);
                        startActivity(Intent.createChooser(sharingIntent, "Partager ..."));
                    }
                }
        );
    }
}
