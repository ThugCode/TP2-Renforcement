package agent.rlagent;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map.Entry;

import environnement.Action;
import environnement.Environnement;
import environnement.Etat;

/**
 * 
 * @author LETOURNEUR - GERLAND
 *
 */
public class QLearningAgent extends RLAgent{
	
	protected HashMap<Etat, HashMap<Action, Double>> Q_Values;
	
	/**
	 * 
	 * @param alpha
	 * @param gamma
	 * @param Environnement
	 */
	public QLearningAgent(double alpha, double gamma,
			Environnement _env) {
		
		super(alpha, gamma, _env);
		
		this.reset();
	}
	
	/**
	 * renvoi la (les) action(s) de plus forte(s) valeur(s) dans l'etat e
	 *  
	 *  renvoi liste vide si aucunes actions possibles dans l'etat 
	 */
	@Override
	public List<Action> getPolitique(Etat e) {
		
		Double maxValue = this.getValeur(e);
		
		ArrayList<Action> liste = new ArrayList<Action>();
		
		HashMap <Action, Double> actions = this.Q_Values.get(e);
		
	    if(actions != null) {
	    	actions.entrySet().stream().forEach((pair) -> {
		        if(maxValue==pair.getValue()) {
		        	liste.add(pair.getKey());
		        }
	    	});
	    }
	    
		return liste;
	}
	
	/**
	 * @return la valeur d'un etat
	 */
	@Override
	public double getValeur(Etat e) {
		
		if(!this.Q_Values.containsKey(e))
			return 0.0;
		
		Double max = Double.MIN_VALUE;
		for(Entry<Action, Double> ad : this.Q_Values.get(e).entrySet()) {
			max = Math.max(max, ad.getValue());
		}
		return max;
	}

	/**
	 * 
	 * @param e
	 * @param a
	 * @return Q valeur du couple (e,a)
	 */
	@Override
	public double getQValeur(Etat e, Action a) {
		if(!this.Q_Values.containsKey(e) || !this.Q_Values.get(e).containsKey(a))
			return 0.0;
		
		return this.Q_Values.get(e).get(a);
	}
	
	/**
	 * setter sur Q-valeur
	 */
	@Override
	public void setQValeur(Etat e, Action a, double d) {
		
		HashMap<Action, Double> temp = new HashMap<Action, Double>();		
		if(this.Q_Values.containsKey(e)) {
			temp = this.Q_Values.get(e);
		}
		
		temp.put(a, d);
		this.Q_Values.put(e, temp);
		
		//mise a jour vmin et vmax pour affichage gradient de couleur
    	vmax = Math.max(d, vmax);
    	vmin = Math.min(d, vmin);
		
		this.notifyObs();
	}
	
	
	/**
	 *
	 * mise a jour de la Q-valeur du couple (e,a) apres chaque interaction <etat e,action a, etatsuivant esuivant, recompense reward>
	 * la mise a jour s'effectue lorsque l'agent est notifie par l'environnement apres avoir realise une action.
	 * @param e
	 * @param a
	 * @param esuivant
	 * @param reward
	 */
	@Override
	public void endStep(Etat e, Action a, Etat esuivant, double reward) {
		
		Double meilleur = this.getValeur(esuivant);
		Double valeur = (1-this.alpha)*this.getQValeur(e,a)+this.alpha*(reward+this.gamma*meilleur);
		this.setQValeur(e, a, valeur);
	}

	@Override
	public Action getAction(Etat e) {
		this.actionChoisie = this.stratExplorationCourante.getAction(e);
		return this.actionChoisie;
	}

	/**
	 * reinitialise les Q valeurs
	 */
	@Override
	public void reset() {
		super.reset();
		this.episodeNb = 0;

		this.vmax = Integer.MIN_VALUE;
        this.vmin = Integer.MAX_VALUE;
		this.Q_Values = new HashMap<Etat, HashMap<Action, Double>>();
		
		this.notifyObs();
	}
}
