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
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;

//import amycorp.parkea.Adapters.CustomGridViewAdapter;
import amycorp.parkea.models.Item;

public class InicioActivity extends AppCompatActivity {

    private Button btnConsultarParqueaderos;
    private Button btnReportarParqueaderos;
    private Button btnRegistrarParqueo;
    private Button btnPerfilUsuario;

    //GridView gridView;
    //ArrayList<Item> gridArray = new ArrayList<Item>();
    //CustomGridViewAdapter customGridAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_inicio);
        // Boton Agrega un nuevo Ingreso el usuario
        btnRegistrarParqueo = (Button) findViewById(R.id.btnRegistrarParqueo);
        // Boton Consulta Los Parqueaderos Por Facultad
        btnConsultarParqueaderos = (Button) findViewById(R.id.btnConsultarParqueos);
        // Boton Reportar(Actualizar) Parqueaderos Por Facultad
        btnReportarParqueaderos = (Button) findViewById(R.id.btnReportarParqueaderos);
        // Boton Ver Perfil usuario
        btnPerfilUsuario = (Button) findViewById(R.id.btnPerfilUsuario);


        // Eventos de botones

        btnRegistrarParqueo.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent facultadesIntent = new Intent(getApplicationContext(), RegistrarParqueoActivity.class);
                startActivity(facultadesIntent);
            }
        });

        btnConsultarParqueaderos.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent facultadesIntent = new Intent(getApplicationContext(), FacultadesActivity.class);
                startActivity(facultadesIntent);
            }
        });

        btnPerfilUsuario.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent facultadesIntent = new Intent(getApplicationContext(), MiPerfilActivity.class);
                startActivity(facultadesIntent);
            }
        });

        btnReportarParqueaderos.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent facultadesIntent = new Intent(getApplicationContext(), ReportarParqueaderosActivity.class);
                startActivity(facultadesIntent);
            }
        });
    }

}
