package com.example.json_apirestful_param_header

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.method.ScrollingMovementMethod
import android.view.View
import android.widget.TextView
import com.android.volley.AuthFailureError
import com.android.volley.Request
import com.android.volley.toolbox.JsonArrayRequest
import com.android.volley.toolbox.Volley
import org.json.JSONObject

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }

    fun cosumirApiConParametros(view: View) {
        val respuestaObtenida = findViewById<TextView>(R.id.txtRespuestaJSON);
        //Para obtener el parámetro Header que ingresará el usuario
        val paramHeaderUser = findViewById<TextView>(R.id.txtParamHeader);

        //Línea que nos permitirá hacer scroll para poder ver todos los registros que traemos de la API
        respuestaObtenida.movementMethod = ScrollingMovementMethod();

        val queue = Volley.newRequestQueue(this)
        val url = "http://www.google.com"

        // Nuestra respuesta ahora es de tipo objeto JsonArrayRequest
        val stringRequest = object: JsonArrayRequest(
            Request.Method.GET, "https://api-uat.kushkipagos.com/transfer/v1/bankList", null,
            { response ->
                //Parseo de los datos para que se puedan leer de mejor manera
                var listaBancos = "";
                for(i in 0 until response.length()){
                    val infoBanco: JSONObject = response.getJSONObject(i);
                    listaBancos = listaBancos + " {\n    code: " + infoBanco.getString("code") +
                            "; \n    name: " + infoBanco.getString("name")+"\n },\n"
                }
                respuestaObtenida.text=listaBancos;
            },
            {
                //Mensaje de error si no se pudo consumir la API
                respuestaObtenida.text="Ha ocurrido un error inesperado\n y no se pudo consumir la API de Bancos KUSHKI :(";
            }){
            @Throws(AuthFailureError::class)
            override fun getHeaders(): MutableMap<String, String> {
                val paramHeader = HashMap<String, String>()
                paramHeader.put("Content-Type", "application/json; utf-8");
                //Especificamos el parámetro que ingresó el usuario
                paramHeader.put("Public-Merchant-Id", paramHeaderUser.text.toString());
                return paramHeader;
            }
        }
        queue.add(stringRequest)
    }
}