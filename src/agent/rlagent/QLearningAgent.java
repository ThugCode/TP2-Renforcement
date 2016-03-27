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
	
	/**
	 * Matrice des valeurs Q
	 */
	protected HashMap<Etat, HashMap<Action, Double>> Q_Values;
	
	/**
	 * 
	 * @param alpha
	 * @param gamma
	 * @param Environnement
	 */
	public QLearningAgent(double alpha, double gamma, Environnement _env) {
		
		super(alpha, gamma, _env);
		this.reset();
	}
	
	/**
	 * Renvoi la (les) action(s) de plus forte(s) valeur(s) dans l'etat e
	 *  
	 *  @param Etat e
	 *  @return liste Liste d'actions (vide si aucune action possible dans l'etat) 
	 */
	@Override
	public List<Action> getPolitique(Etat e) {
		
		ArrayList<Action> liste = new ArrayList<Action>();
		
		//Récupération de la valeur maximum
		Double maxValue = this.getValeur(e);
		
		//Parcours des actions de l'état, si la valeur de l'action 
		//est égale à la valeur max, on ajoute l'action à la liste
		HashMap <Action, Double> actions = this.Q_Values.get(e);
	    if(actions != null) {
	    	actions.entrySet().stream().forEach((pair) -> {
		        if(maxValue.equals(pair.getValue())) {
		        	liste.add(pair.getKey());
		        }
	    	});
	    }
	    
		return liste;
	}
	
	/**
	 * Retourne la valeur max des actions dans l'état associé
	 * 
	 * @param Etat e
	 * @return la valeur d'un etat
	 */
	@Override
	public double getValeur(Etat e) {
		
		//Si l'action n'est pas connu dans l'état, retourne 0
		if(!this.Q_Values.containsKey(e))
			return 0.0;
		
		Double max = 0.0;
		for(Entry<Action, Double> ad : this.Q_Values.get(e).entrySet()) {
			max = Math.max(max, ad.getValue());
		}
		return max;
	}

	/**
	 * Retourne la Q valeur pour un état donné et une action donnée
	 * 
	 * @param Etat e
	 * @param Action a
	 * @return Q valeur du couple (e,a)
	 */
	@Override
	public double getQValeur(Etat e, Action a) {
		
		//Si valeur inconnu, retourne 0
		if(!this.Q_Values.containsKey(e) 
		|| !this.Q_Values.get(e).containsKey(a))
			return 0.0;
		
		return this.Q_Values.get(e).get(a);
	}
	
	/**
	 * Setter sur Q-valeur
	 * 
	 * @param Etat e
	 * @param Action a
	 * @param double d
	 */
	@Override
	public void setQValeur(Etat e, Action a, double d) {
		
		HashMap<Action, Double> temp = new HashMap<Action, Double>();		
		if(this.Q_Values.containsKey(e)) {
			temp = this.Q_Values.get(e);
		}
		temp.put(a, d);
		this.Q_Values.put(e, temp);
		
		//Mise a jour vmin et vmax pour affichage gradient de couleur
    	vmax = Math.max(d, vmax);
    	vmin = Math.min(d, vmin);
		
		this.notifyObs();
	}
	
	
	/**
	 * Mise a jour de la Q-valeur du couple (e,a) après chaque interaction 
	 * <etat e, action a, etatsuivant esuivant, recompense reward>
	 * La mise à jour s'effectue lorsque l'agent est notifie par 
	 * l'environnement après avoir réalisé une action.
	 * 
	 * @param e
	 * @param a
	 * @param esuivant
	 * @param reward
	 */
	@Override
	public void endStep(Etat e, Action a, Etat esuivant, double reward) {
		
		Double valeur = (1-this.alpha)*this.getQValeur(e,a)+this.alpha*(reward+this.gamma*this.getValeur(esuivant));
		this.setQValeur(e, a, valeur);
	}

	/**
	 * 
	 * 
	 * @param e
	 */
	@Override
	public Action getAction(Etat e) {
		this.actionChoisie = this.stratExplorationCourante.getAction(e);
		return this.actionChoisie;
	}

	/**
	 * Réinitialise les Q valeurs, vmin, vmax
	 */
	@Override
	public void reset() {
		super.reset();

		this.vmax = Double.MIN_VALUE;
        this.vmin = Double.MAX_VALUE;
		this.Q_Values = new HashMap<Etat, HashMap<Action, Double>>();
		
		this.notifyObs();
	}
}
