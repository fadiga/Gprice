package fadcorp.mprice;

import android.app.Activity;
import android.os.Bundle;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.widget.Button;

/**
 * Created by fad on 21/03/15.
 */
public class ConvirtDevise extends Activity {


    private static final String TAG = Constants.getLogTag("ConvirtDevise");
    private Button versionButton;
    private WebView webView;
    boolean isOnline;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTitle(R.string.action_convert);
        setContentView(R.layout.convert_devises);

        setupUI();
    }

    protected void setupUI() {

        final Activity activity = this;
        webView = (WebView) findViewById(R.id.webView);
        webView.getSettings().setJavaScriptEnabled(true);
        webView.getSettings().setJavaScriptCanOpenWindowsAutomatically(true);
        webView.loadUrl("file:///android_asset/currency_rates.html");

        webView.setWebChromeClient(new WebChromeClient(){

            public void onProgressChanged(WebView view, int progress) {
                activity.setTitle(R.string.loading);
                activity.setProgress(progress * 100);
                if(progress == 100)
                    activity.setTitle(R.string.action_convert);
            }
        });

        webView.reload();
    }
}
