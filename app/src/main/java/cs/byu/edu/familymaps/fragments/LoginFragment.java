package cs.byu.edu.familymaps.fragments;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import org.json.JSONException;
import java.io.IOException;
import cs.byu.edu.familymaps.R;
import cs.byu.edu.familymaps.model.ServerAccess;

public class LoginFragment extends Fragment
{
    private static final String LOG_TAG = "LoginFragment";
    private Context context;

    private LoginListener parent;
    private Button mLoginButton;
    private EditText mUsername;
    private EditText mPassword;
    private EditText mHost;
    private EditText mPort;

    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        parent = (LoginListener) getActivity();
        context = getContext();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstance)
    {
        Log.d(LOG_TAG, "OnCreate() called");
        View v = inflater.inflate(R.layout.fragment_login, container, false);

        mLoginButton = (Button) v.findViewById(R.id.login_button);
        mUsername = (EditText) v.findViewById(R.id.username);
        mUsername.setText("username");
        mPassword = (EditText) v.findViewById(R.id.password);
        mPassword.setText("password");
        mHost = (EditText) v.findViewById(R.id.host);
        mHost.setText("192.168.253.91");
        mPort = (EditText) v.findViewById(R.id.port);
        mPort.setText("8080");

        mLoginButton.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                String username = mUsername.getText().toString();
                String password = mPassword.getText().toString();
                String host = mHost.getText().toString();
                String port = mPort.getText().toString();

                Toast.makeText(context, "Connecting...", Toast.LENGTH_LONG).show();
                new LoginTask().execute(host, port, username, password);
            }
        });

        return v;
    }

    private class LoginTask extends AsyncTask<String, Void, String>
    {
        @Override
        protected String doInBackground(String... params)
        {
            try
            {
                String response = ServerAccess.login(params[0], params[1], params[2], params[3]);
                if(!response.contains("emessage"))
                {
                    if(!ServerAccess.getEvents() || !ServerAccess.getPersons())
                    {
                        response = "failed to connect";
                    }
                }

                return response;

            }
            catch (IOException e)
            {
                return e.getMessage();
            }
            catch (JSONException e)
            {
                return "invalid json";
            }
        }

        @Override
        protected void onPostExecute(String response)
        {
            if(response.contains("emessage"))
            {
                Log.d(LOG_TAG, response);
                Toast.makeText(context, "Invalid username or password", Toast.LENGTH_SHORT).show();
            }
            else if(response.contains("failed to connect"))
            {
                Toast.makeText(context, "Could not connect to server", Toast.LENGTH_SHORT).show();
            }
            else if(response.contains("invalid json"))
            {
                Toast.makeText(context, "Server error: invalid response", Toast.LENGTH_SHORT).show();
            }
            else
            {
                parent.login();
                AmazonMapFragment.sync();
                Toast.makeText(getContext(), "Finished loading", Toast.LENGTH_SHORT).show();
            }
        }
    }

    public interface LoginListener
    {
        void login();
    }
}
