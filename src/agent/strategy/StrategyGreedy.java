package agent.strategy;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import agent.rlagent.RLAgent;
import environnement.Action;
import environnement.Etat;

/**
 * Strategie qui renvoit une action aleatoire avec probabilite epsilon, une action gloutonne (qui suit la politique de l'agent) sinon
 * Cette classe a acces a un RLAgent par l'intermediaire de sa classe mere.
 * 
 * @author GERLAND - LETOURNEUR
 *
 */
public class StrategyGreedy extends StrategyExploration{
	
	protected double epsilon;
	
	private Random rand = new Random();
	
	/**
	 * Constructeur
	 */
	public StrategyGreedy(RLAgent agent,double epsilon) {
		super(agent);
		
		this.setEpsilon(epsilon);
	}

	/**
	 * Retourne l'action que l'agent doit exécuter
	 * 
	 * @param _e
	 * @return action selectionnee par la strategie d'exploration
	 */
	@Override
	public Action getAction(Etat _e) {
		
		//Si le random < epsilon, on agit aléatoirement
		if(rand.nextDouble() < epsilon) {
			List<Action> actions = this.getAgent().getActionsLegales(_e);
			if(actions.size() <= 0) return null;
			return actions.get(rand.nextInt(actions.size()));
		}
		
		//Sinon on suit la politique
		ArrayList<Action> liste = (ArrayList<Action>) this.agent.getPolitique(_e);
		if(liste.size() <= 0) return null;
		int i = rand.nextInt(liste.size());
        return liste.get(i);
	}

	/**
	 * Setter epsilon
	 * 
	 * @param epsilon
	 */
	public void setEpsilon(double epsilon) {
		this.epsilon = epsilon;
	}

}
