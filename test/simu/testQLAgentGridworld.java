package simu;

import javax.swing.SwingUtilities;

import vueGridworld.VueGridworldRL;
import agent.rlagent.QLearningAgent;
import agent.rlagent.RLAgent;
import environnement.gridworld.GridworldEnvironnement;
import environnement.gridworld.GridworldMDP;

public class testQLAgentGridworld {
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		  SwingUtilities.invokeLater(new Runnable(){
				public void run(){

					GridworldMDP gmdp = GridworldMDP.getBookGrid();
					
					GridworldEnvironnement g = new GridworldEnvironnement(gmdp);
					gmdp.setProba(0.1);
					double gamma=0.9;
					double alpha=0.1;
					
					RLAgent a = new QLearningAgent(alpha,gamma,g);
					a.DISPEPISODE = true;
					VueGridworldRL vue = new VueGridworldRL(g,a);			
									
					vue.setVisible(true);
				
				}
			});

	}
}
