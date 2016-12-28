package amycorp.parkea;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;

import amycorp.parkea.Adapters.CustomGridViewAdapter;
import amycorp.parkea.models.Item;

public class InicioActivity extends AppCompatActivity {
    private Button btnConsultarParqueaderos;
    private Button btnReportarParqueaderos;

    GridView gridView;
    ArrayList<Item> gridArray = new ArrayList<Item>();
    CustomGridViewAdapter customGridAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_inicio);
        btnConsultarParqueaderos = (Button) findViewById(R.id.btn_consultar_parqueaderos);
        btnReportarParqueaderos = (Button) findViewById(R.id.btn_reportar_parqueaderos);

        btnConsultarParqueaderos.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent facultadesIntent = new Intent(getApplicationContext(), FacultadesActivity.class);
                startActivity(facultadesIntent);
            }
        });


        //set grid view item
        //Bitmap homeIcon = BitmapFactory.decodeResource(this.getResources(), R.drawable.ic_extension_black_24dp);
        //Bitmap userIcon = BitmapFactory.decodeResource(this.getResources(), R.drawable.ic_access_time_black_24dp);

//        gridArray.add(new Item(homeIcon,"Home"));
//        gridArray.add(new Item(userIcon,"User"));
//        gridArray.add(new Item(homeIcon,"House"));
//        gridArray.add(new Item(userIcon,"Friend"));
//
//        gridView = (GridView) findViewById(R.id.gridView1);
//        customGridAdapter = new CustomGridViewAdapter(this, R.layout.row_grid, gridArray);
//        gridView.setAdapter(customGridAdapter);

    }

}
