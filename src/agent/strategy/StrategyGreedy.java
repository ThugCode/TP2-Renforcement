package agent.strategy;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import agent.rlagent.RLAgent;
import environnement.Action;
import environnement.Etat;
import environnement.gridworld.ActionGridworld;
/**
 * Strategie qui renvoit une action aleatoire avec probabilite epsilon, une action gloutonne (qui suit la politique de l'agent) sinon
 * Cette classe a acces a un RLAgent par l'intermediaire de sa classe mere.
 * @author lmatignon
 *
 */
public class StrategyGreedy extends StrategyExploration{
	
	protected double epsilon;
	
	private Random rand = new Random();
	
	public StrategyGreedy(RLAgent agent,double epsilon) {
		super(agent);
		
		this.setEpsilon(epsilon);
	}

	/**
	 * @return action selectionnee par la strategie d'exploration
	 */
	@Override
	public Action getAction(Etat _e) {
		
		if(rand.nextDouble() < epsilon) {
			List<Action> actions = this.getAgent().getActionsLegales(_e);
			return actions.get(rand.nextInt(actions.size()));
		}
		
		ArrayList<Action> liste = (ArrayList<Action>) this.agent.getPolitique(_e);
		if(liste.size() <= 0)
			return null;
		
		int i = rand.nextInt(liste.size());
        return liste.get(i);
	}

	public void setEpsilon(double epsilon) {
		this.epsilon = epsilon;
	}

}
