package agent.strategy;

import java.util.List;
import java.util.Random;

import agent.rlagent.RLAgent;
import environnement.Action;
import environnement.Etat;

/**
 * Strategie qui renvoit une action aleatoire avec probabilité epsilon, 
 * une action gloutonne (qui suit la politique de l'agent) sinon
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
		
		List<Action> actions = null;
		
		//Retourne null si l'état est absorbant
		if(this.getAgent().getEnv().estAbsorbant())
			return null;
		
		//Si le random < epsilon, on agit aléatoirement
		if(rand.nextDouble() < epsilon) {
			actions = this.getAgent().getActionsLegales(_e);
		} else { //Sinon on suit la politique
			actions = this.agent.getPolitique(_e);
		}
		
		if(actions.size() <= 0) return null;
		return actions.get(rand.nextInt(actions.size()));
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
