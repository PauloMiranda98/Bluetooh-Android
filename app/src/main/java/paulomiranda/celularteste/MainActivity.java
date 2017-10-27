package paulomiranda.celularteste;

import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity{

    private static final int REQUEST_ENABLE_BT = 1;
    private BluetoothAdapter meuBluetooth = null;

    private ManipulaBluetooh manipulaBluetooh;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        meuBluetooth = BluetoothAdapter.getDefaultAdapter();

        if (!meuBluetooth.isEnabled()) {
            Intent novoIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            startActivityForResult(novoIntent, REQUEST_ENABLE_BT);
        }

        manipulaBluetooh = new ManipulaBluetooh("98:D3:31:FC:3C:ED");
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        switch (requestCode) {

            case REQUEST_ENABLE_BT:
                if (resultCode == Activity.RESULT_OK) {
                    Toast.makeText(getApplicationContext(), "Bluetooth esta ativado", Toast.LENGTH_LONG).show();
                } else {
                    Toast.makeText(getApplicationContext(), "Bluetooth nao esta ativado", Toast.LENGTH_LONG).show();
                    finish();
                }
                break;
        }
    }

    public void onEnviar(View v) {
        if(!meuBluetooth.isEnabled()) {
            Toast.makeText(this, "Bluetooh desligado!", Toast.LENGTH_SHORT).show();
            return;
        }

        if(!manipulaBluetooh.verificarConexao()){
            Toast.makeText(this, "Bluetooh desconectado", Toast.LENGTH_SHORT).show();
            return;
        }

        EditText edt = (EditText) findViewById(R.id.edt_texto);
        if(edt.getText().length() > 0)
            manipulaBluetooh.puts(edt.getText().toString());

    }

    public void onReceber(View v) {
        if(!meuBluetooth.isEnabled()) {
            Toast.makeText(this, "Bluetooh desligado!", Toast.LENGTH_SHORT).show();
            return;
        }

        if(!manipulaBluetooh.verificarConexao()){
            Toast.makeText(this, "Bluetooh desconectado", Toast.LENGTH_SHORT).show();
            return;
        }

        char c = manipulaBluetooh.getc(); // caso não tenha nenhum caractere no buffering, ele irá congelar a tela até o caracter chegar.

        TextView saida = (TextView) findViewById(R.id.txt_saida);
        saida.setText("Saída: " + c);
    }

    public void onConectar(View v) {

        if(meuBluetooth.isEnabled())
            manipulaBluetooh.conectar();

    }

    public void onDesconectar(View v) {

        if(meuBluetooth.isEnabled())
            manipulaBluetooh.desconectar();

    }
}
