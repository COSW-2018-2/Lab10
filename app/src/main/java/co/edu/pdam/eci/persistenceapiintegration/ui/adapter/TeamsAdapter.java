package co.edu.pdam.eci.persistenceapiintegration.ui.adapter;
import android.app.Activity;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import co.edu.pdam.eci.persistenceapiintegration.R;
import co.edu.pdam.eci.persistenceapiintegration.data.entity.Team;
import co.edu.pdam.eci.persistenceapiintegration.network.TeamImageGet;

public class TeamsAdapter extends RecyclerView.Adapter <TeamsAdapter.ViewHolder>{
    private List<Team> teams;
    Activity activity;

    public TeamsAdapter(List teams, Activity activity){
        this.teams = teams;
          this.activity = activity;

    }

    @Override
    public TeamsAdapter.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View itemView = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.card, viewGroup, false);
        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Team team = teams.get(position);
        holder.name.setText(team.getName());
        holder.shortName.setText(team.getShortName());
        try {
            ExecutorService executorService = Executors.newFixedThreadPool( 1 );
            executorService.execute(new TeamImageGet(holder,team.getImageUrl(),activity));

        } catch (Exception e) {
            e.printStackTrace();
        }
    }



    @Override
    public int getItemCount() {
        return teams.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView name;
        TextView shortName;
        public ImageView image;

        public ViewHolder(View itemView) {
            super(itemView);
            image = (ImageView) itemView.findViewById(R.id.logo);
            name = (TextView) itemView.findViewById(R.id.name);
            shortName = (TextView) itemView.findViewById(R.id.shortName);
        }
    }

}
