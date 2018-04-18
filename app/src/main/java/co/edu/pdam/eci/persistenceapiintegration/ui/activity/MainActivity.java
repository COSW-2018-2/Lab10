package co.edu.pdam.eci.persistenceapiintegration.ui.activity;

import android.app.Activity;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import android.support.v7.widget.LinearLayoutManager;


import co.edu.pdam.eci.persistenceapiintegration.R;
import co.edu.pdam.eci.persistenceapiintegration.data.DBException;
import co.edu.pdam.eci.persistenceapiintegration.data.OrmModel;
import co.edu.pdam.eci.persistenceapiintegration.data.dao.TeamDao;
import co.edu.pdam.eci.persistenceapiintegration.data.entity.Team;
import co.edu.pdam.eci.persistenceapiintegration.network.NetworkException;
import co.edu.pdam.eci.persistenceapiintegration.network.RequestCallback;
import co.edu.pdam.eci.persistenceapiintegration.network.RetrofitNetwork;
import co.edu.pdam.eci.persistenceapiintegration.ui.adapter.TeamsAdapter;

import android.support.v7.widget.RecyclerView;


public class MainActivity extends AppCompatActivity {
    private OrmModel ormModel;
    private RetrofitNetwork retrofitNetwork;
    private RecyclerView recyclerView;
    private TeamsAdapter teamsAdapter;
    private List<Team> teams;
    private Activity act;

    @Override
    protected void onCreate( Bundle savedInstanceState ) {
        super.onCreate( savedInstanceState );
        act=this;
        setContentView( R.layout.activity_main );
        ormModel = new OrmModel();
        ormModel.init(this);
        final TeamDao teamDao = ormModel.getTeamDao();
        ExecutorService executorService = Executors.newFixedThreadPool( 1 );

        executorService.execute( new Runnable()
        {
            @Override
            public void run()
            {
                try
                {
                    retrofitNetwork = new RetrofitNetwork();
                    retrofitNetwork.getTeams(new RequestCallback<List<Team>>() {
                        @Override
                        public void onSuccess(List<Team> response) {
                            for(Team t:response){
                                try {
                                    System.out.println(t);
                                    teamDao.create(t);
                                } catch (DBException e) {
                                    e.printStackTrace();
                                }
                            }
                            act.runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    loadTeams(teamDao);
                                }
                            });
                        }
                        @Override
                        public void onFailed(NetworkException e) {
                            e.printStackTrace();
                        }
                    });
                }
                catch ( Exception e )
                {
                    e.printStackTrace();
                }
            }
        } );
        loadTeams(teamDao);
    }

    private void loadTeams(TeamDao teamDao) {
        try {
            teams=teamDao.getAll();
            configureRecyclerView();
        } catch (DBException e) {
            e.printStackTrace();
        }
    }

    private void configureRecyclerView() throws DBException {
        teamsAdapter = new TeamsAdapter(teams,this);
        recyclerView = (RecyclerView) findViewById( R.id.recyclerView );
        recyclerView.setHasFixedSize(true);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager( this );
        recyclerView.setLayoutManager( layoutManager );
        recyclerView.setAdapter( teamsAdapter );
    }

}
