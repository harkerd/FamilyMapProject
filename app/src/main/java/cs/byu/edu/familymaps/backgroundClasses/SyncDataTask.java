package cs.byu.edu.familymaps.backgroundClasses;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

import java.io.IOException;

import cs.byu.edu.familymaps.model.ServerAccess;

public abstract class SyncDataTask extends AsyncTask<Void, Void, Boolean>
{
    public Context context;
    private static String LOG_TAG;

    public SyncDataTask(Context context, String LOG_TAG)
    {
        this.context = context;
        this.LOG_TAG = LOG_TAG;
        Toast.makeText(context, "Retrieving data...", Toast.LENGTH_LONG).show();
    }

    @Override
    protected Boolean doInBackground(Void... params)
    {
        try
        {
            return ServerAccess.getEvents() && ServerAccess.getPersons();
        }
        catch (IOException e)
        {
            Log.d(LOG_TAG, e.getMessage());
            return false;
        }
    }

    @Override
    protected abstract void onPostExecute(Boolean successful);
}
