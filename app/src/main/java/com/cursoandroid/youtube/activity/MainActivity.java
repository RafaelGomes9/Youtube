package com.cursoandroid.youtube.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.SearchView;

import com.cursoandroid.youtube.R;
import com.cursoandroid.youtube.adapter.AdapterVideo;
import com.cursoandroid.youtube.api.YoutubeService;
import com.cursoandroid.youtube.helper.RetrofitConfig;
import com.cursoandroid.youtube.helper.YoutubeConfig;
import com.cursoandroid.youtube.listener.RecyclerItemClickListener;
import com.cursoandroid.youtube.model.Item;
import com.cursoandroid.youtube.model.Resultado;
import com.cursoandroid.youtube.model.Video;
import com.google.android.youtube.player.YouTubePlayerView;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class MainActivity extends AppCompatActivity {


    private RecyclerView recyclerVideos;

    private List<Item> videos= new ArrayList<>();
    private Resultado resultado;



    private AdapterVideo adapterVideo;


    private Retrofit retrofit;














    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);






















        Toolbar toolbar;
        toolbar= findViewById(R.id.toolbar);
        toolbar.setTitle("Youtube");
        setSupportActionBar(toolbar);






        retrofit= RetrofitConfig.getRetrofit();

        recuperarVideos("");






    }







    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {


        getMenuInflater().inflate(R.menu.menu,menu);
        MenuItem menuItem= menu.findItem(R.id.action_pesquisar);
        SearchView searchView= (SearchView) menuItem.getActionView();





            searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {


                recuperarVideos(query);


                return true;
            }



            @Override
            public boolean onQueryTextChange(String newText) {



                

















                return false;
            }
        });




            searchView.addOnAttachStateChangeListener(new View.OnAttachStateChangeListener() {
                @Override
                public void onViewAttachedToWindow(View v) {






                }

                @Override
                public void onViewDetachedFromWindow(View v) {


                    recuperarVideos("");





                }
            });







        return super.onCreateOptionsMenu(menu);
    }








    private void recuperarVideos(String pesquisa)
    {

        String q= pesquisa.replaceAll(" ","+");




        YoutubeService youtubeService= retrofit.create(YoutubeService.class);
        youtubeService.recuperarVideos(


                "snippet","date","20",
                YoutubeConfig.CHAVE_YOUTUBE_API, YoutubeConfig.CANAL_ID,q








        ).enqueue(new Callback<Resultado>() {
            @Override
            public void onResponse(Call<Resultado> call, Response<Resultado> response) {


             //  Log.i("resultado","resultado: "+response.toString());

                resultado= response.body();


                videos= resultado.items;
                configurarRecyclerView();





            }




            @Override
            public void onFailure(Call<Resultado> call, Throwable t) {














            }
        });














    }



public void configurarRecyclerView()
{


    adapterVideo= new AdapterVideo(videos,this);
    recyclerVideos= findViewById(R.id.recyclerVideos);
    recyclerVideos.setHasFixedSize(true);
    recyclerVideos.setLayoutManager(new LinearLayoutManager(this));
    recyclerVideos.setAdapter(adapterVideo);



       recyclerVideos.addOnItemTouchListener(new RecyclerItemClickListener(this, recyclerVideos, new RecyclerItemClickListener.OnItemClickListener() {
        @Override
        public void onItemClick(View view, int position) {


            Item video= videos.get(position);
            String idVideo= video.id.videoId;


            Intent i= new Intent(MainActivity.this,PlayerActivity.class);
            i.putExtra("idVideo",idVideo);
            startActivity(i);










        }

        @Override
        public void onLongItemClick(View view, int position) {






        }

        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {







        }
    }));










}




}