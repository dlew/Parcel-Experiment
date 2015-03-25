package net.nemanjakovacevic.parcelexperiment;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.os.Parcel;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.ArrayList;


public class MainActivity extends ActionBarActivity {

    public static final NumberFormat NUMBER_FORMAT = new DecimalFormat("###,###.##");
    public static long START_TIMESTAMP;
    public static boolean USE_SERIALIZATION;

    ArrayList<EmptyObject> mList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.recyclerview);
        recyclerView.setHasFixedSize(true);
        LinearLayoutManager llm = new LinearLayoutManager(this);
        llm.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(llm);
        ((EditText)findViewById(R.id.list_size)).setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_DONE || actionId == EditorInfo.IME_ACTION_NEXT) {
                    InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
                    View focus = MainActivity.this.getCurrentFocus();
                    if(focus != null){
                        imm.hideSoftInputFromWindow(focus.getWindowToken(), 0);
                    }
                    generateButtonClicked(null);
                    return true;
                }
                return false;
            }
        });
    }

    public void launchButtonClicked(View view){
        Toast.makeText(this, "I broke this to test Parcelable!", Toast.LENGTH_SHORT).show();
    }

    public void generateButtonClicked(View view){
        try {
            EditText editText = (EditText) findViewById(R.id.list_size);
            boolean sameInstance = ((CheckBox)findViewById(R.id.use_same_instance)).isChecked();
            int size = Integer.parseInt(editText.getText().toString());
            if(size <= 0){
                throw new NumberFormatException();
            }

            mList = generateEmptyObjectList(size, sameInstance);

            ((RecyclerView)findViewById(R.id.recyclerview)).setAdapter(new EmptyObjectAdapter(mList));

            byte[] parceleableByteArray = marshallToByteArrayViaParceler(mList);
            byte[] serializedByteArray = marshallToByteArrayViaSerialization(mList);

            String message = "Generated an ArrayList of EmptyObject instances of size: %s \n Size of marshalled parcel byte array: %s \n Size of serialized byte array: %s";
            message = String.format(message, size, NUMBER_FORMAT.format(parceleableByteArray.length), NUMBER_FORMAT.format(serializedByteArray.length));

            showAlert(message);

        } catch (NumberFormatException e) {
            Toast.makeText(this, "Please enter positive integer number for list size", Toast.LENGTH_LONG).show();
        }
    }

    private byte[] marshallToByteArrayViaSerialization(ArrayList<EmptyObject> list) {
        try {
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            ObjectOutputStream oos = new ObjectOutputStream(baos);
            oos.writeObject(new Package(list));
            return baos.toByteArray();
        } catch (IOException e) {
            showAlert(e.getMessage());
            return null;
        }
    }

    private byte[] marshallToByteArrayViaParceler(ArrayList<EmptyObject> list) {
//        Parcelable parcelable = Parcels.wrap(new Package(list));
        Parcel parcel = Parcel.obtain();
        parcel.writeTypedList(list);
//        parcelable.writeToParcel(parcel, 0);
        byte[] bytes = parcel.marshall();
        return bytes;

    }

    private ArrayList<EmptyObject> generateEmptyObjectList(int size, boolean useSameInstance){
        ArrayList<EmptyObject> list = new ArrayList<>();
        EmptyObject emptyObject = new EmptyObject();
        for(int i=0; i<size; i++){
            if(!useSameInstance){
                emptyObject = new EmptyObject();
            }
            list.add(emptyObject);
        }
        return list;
    }

    private void showAlert(String message){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage(message);
        builder.setPositiveButton(getString(android.R.string.ok), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        builder.create().show();
    }

}
