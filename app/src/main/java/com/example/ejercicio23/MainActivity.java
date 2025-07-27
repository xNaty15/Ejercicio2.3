package com.example.ejercicio23;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.ejercicio23.modelo.Fotografía;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private ImageView imageViewPreview;
    private EditText editTextDescription;
    private Button btnPickImage, btnSave;
    private ListView listView;
    private PhotoAdapter adapter;
    private List<Fotografía> photoList;
    private DatabaseHelper databaseHelper;
    private byte[] imageBytes;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);

        imageViewPreview = findViewById(R.id.imageViewPreview);
        editTextDescription = findViewById(R.id.editTextDescription);
        btnPickImage = findViewById(R.id.btnPickImage);
        btnSave = findViewById(R.id.btnSave);
        listView = findViewById(R.id.listView);

        databaseHelper = new DatabaseHelper(this);
        loadPhotos();

        btnPickImage.setOnClickListener(v -> pickImageFromGallery());

        btnSave.setOnClickListener(v -> {
            try {
                databaseHelper = new DatabaseHelper(this);
                String description = editTextDescription.getText().toString().trim();
                if (imageBytes != null && !description.isEmpty()) {
                    savePhoto(imageBytes, description);
                } else {
                    Toast.makeText(this, "Selecciona una imagen y agrega una descripción", Toast.LENGTH_SHORT).show();
                }
            }catch(Exception e){
                Toast.makeText(this,"Imagen demasiado grande",Toast.LENGTH_LONG).show();
                Log.d(e.getMessage(),"errorSUbirImagen");
            }
        });

    }

    private void loadPhotos() {
        databaseHelper = new DatabaseHelper(this);
        photoList = databaseHelper.getAllPhotographs();
        adapter = new PhotoAdapter(this, photoList);
        listView.setAdapter(adapter);
    }

    private void pickImageFromGallery() {
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(intent, 100);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 100 && resultCode == RESULT_OK && data != null) {
            Uri imageUri = data.getData();
            try {
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), imageUri);
                imageViewPreview.setImageBitmap(bitmap);

                // Convertir la imagen a byte[],
                ByteArrayOutputStream stream = new ByteArrayOutputStream();
                bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);
                imageBytes = stream.toByteArray();

            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private void savePhoto(byte[] image, String description) {
        databaseHelper = new DatabaseHelper(this);
        Fotografía photo = new Fotografía(image, description);
        databaseHelper.addPhotograph(photo);

        editTextDescription.setText(""); // Limpiar campos
        imageViewPreview.setImageResource(android.R.color.darker_gray);
        imageBytes = null;

        loadPhotos(); // Recargar lista
        Toast.makeText(this, "Foto guardada", Toast.LENGTH_SHORT).show();
    }
}