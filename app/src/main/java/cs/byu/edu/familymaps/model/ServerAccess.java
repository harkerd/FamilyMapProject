package cs.byu.edu.familymaps.model;

import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedWriter;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;

public class ServerAccess
{
    private static String authorizationToken = "";
    private static String host;
    private static String port;
    private static Gson gson = new Gson();

    private static byte[] getUrlBytes(String urlSpec) throws IOException
    {
        URL url = new URL("http://" + urlSpec);
        HttpURLConnection connection = (HttpURLConnection)url.openConnection();

        try
        {
            if(!authorizationToken.equals(""))
            {
                connection.setRequestProperty("Authorization", authorizationToken);
            }
            ByteArrayOutputStream out = new ByteArrayOutputStream();
            InputStream in = connection.getInputStream();

            if(connection.getResponseCode() != HttpURLConnection.HTTP_OK)
            {
                throw new IOException(connection.getResponseMessage() + ": with " + urlSpec);
            }

            int bytesRead = 0;
            byte[] buffer = new byte[1024];
            while((bytesRead = in.read(buffer)) > 0)
            {
                out.write(buffer, 0, bytesRead);
            }
            out.close();
            return out.toByteArray();
        }
        finally
        {
            connection.disconnect();
        }
    }

    private static byte[] postUrlBytes(String urlSpec, String body) throws IOException
    {
        URL url = new URL("http://" + urlSpec);
        HttpURLConnection connection = (HttpURLConnection)url.openConnection();

        try
        {
            connection.setRequestMethod("POST");
            connection.setDoInput(true);
            connection.setDoOutput(true);


            BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(connection.getOutputStream(), "UTF-8"));
            writer.write(body);
            writer.flush();
            writer.close();

            ByteArrayOutputStream out = new ByteArrayOutputStream();
            InputStream in = connection.getInputStream();

            if(connection.getResponseCode() != HttpURLConnection.HTTP_OK)
            {
                throw new IOException(connection.getResponseMessage() + ": with " + urlSpec);
            }

            int bytesRead = 0;
            byte[] buffer = new byte[1024];
            while((bytesRead = in.read(buffer)) > 0)
            {
                out.write(buffer, 0, bytesRead);
            }
            out.close();
            return out.toByteArray();
        }
        finally
        {
            connection.disconnect();
        }

    }

    private static String getURLString(String urlSpec) throws IOException
    {
        return new String(getUrlBytes(urlSpec));
    }

    private static String getURLString(String urlSpec, String body) throws IOException
    {
        return new String(postUrlBytes(urlSpec, body));
    }

    public static String login(String h, String p, String username, String password) throws IOException, JSONException
    {
        host = h;
        port = p;
        username = "username:\"" + username + "\"";
        password = "password:\"" + password + "\"";

        String body = "{" + username + "," + password + "}";
        String responseBody = getURLString(host + ":" + port + "/user/login", body);

        if(!responseBody.contains("emessage"))
        {
            setLoginValues(responseBody);
        }

        return responseBody;
    }

    public static String register(String h, String p, String username, String password, String email,
                            String firstName, String lastName) throws IOException
    {
        host = h;
        port = p;

        username = "username:\"" + username + "\"";
        password = "password:\"" + password + "\"";
        email = "email:\"" + email + "\"";
        firstName = "firstName:\"" + firstName + "\"";
        lastName = "lastName:\"" + lastName + "\"";

        String body = "{" + username + ",\n" + password + ",\n" + email + ",\n" + firstName + ",\n" + lastName + "}";
        getURLString(host + ":" + port + "/user/register", body);

        fill(host, port, UserData.username);

        return "Not being used";
    }

    public static String fill(String h, String p, String username) throws IOException
    {
        host = h;
        port = p;

        return getURLString(host + ":" + port + "/fill/" + username + "/");
    }

    public static boolean getEvents() throws IOException
    {
        String response = getURLString(host + ":" + port + "/event/");
        UserData.events = gson.fromJson(response, Events.class);
        return UserData.events.data != null;
    }

    public static boolean getPersons() throws IOException
    {
        String response = getURLString(host + ":" + port + "/person/");
        UserData.persons = gson.fromJson(response, Persons.class);
        return UserData.persons.data != null;
    }

    public static void setLoginValues(String loginResponse) throws JSONException
    {
        JSONObject loginValues = new JSONObject(loginResponse);
        if(!loginValues.has("userName") || !loginValues.has("Authorization"))
        {
            throw new JSONException("Invalid data received");
        }

        UserData.username = loginValues.getString("userName");
        authorizationToken = loginValues.getString("Authorization");
    }
}
