/*Classe codée par Julien/Walid 
 */
package abstraction.distributeur.europe;
import abstraction.fourni.v0.*;
import abstraction.distributeur.amerique.DistribClient;
import abstraction.fourni.*;
import java.util.ArrayList;
import java.util.List;
public class Distributeur implements Acteur,IDistributeur,DistribClient{
	private Vente derniereVente; // derniere vente effectuee sur le marche
	private Stock stock;
	private Indicateur stockI;
	private Indicateur fondsI;
	private String nom;
	private Tresorerie fonds;
	private Journal journal;
	private int increment;
	private double prixMoyen;

	
	public Distributeur(Vente vente, Stock stock, double qteDemandee, Tresorerie fonds){ // penser à redocoder en enlevant les arguments du constructeur
		this.derniereVente = vente;
		this.stock = stock;
		this.fonds=fonds;
	}
	
	public Distributeur(){
		this.nom = "Distributeur europe";
		this.derniereVente = new Vente(0.002,2);
		this.stock = new Stock();
		this.stock.ajoutStock(40000);
		this.fonds = new Tresorerie(80000);
 	    this.journal = new Journal("Journal de "+this.nom);
 	    Monde.LE_MONDE.ajouterJournal(this.journal);
		this.increment = 0;
		this.prixMoyen = 0;
		this.fondsI = new Indicateur("2_DISTR_EU_fonds", this, 80000);
		this.stockI = new Indicateur("2_DISTR_EU_stocks", this, 40000);
    	Monde.LE_MONDE.ajouterIndicateur( this.fondsI );
    	Monde.LE_MONDE.ajouterIndicateur( this.stockI );

        
	}
	
	public void incrementer(){
		this.increment++;
	}
	
	public void incrementZero(){
		this.increment=0;
	}
	public Indicateur getIndicateurStock(){
		return this.fondsI;
	}
	
	public Indicateur getSolde(){
		return this.stockI;
	}


	public Vente getDerniereVente() {
		return derniereVente;
	}


	public void setVente(Vente vente) {
		this.derniereVente = vente;
	}

	public Journal getJournal(){
		return this.journal;
	}
	
	public double getPrixMax(){
		double prixTransfo;
		prixTransfo = this.getDerniereVente().getPrix();
		if(this.stock.totalStock()>300){
			return (((prixTransfo*1.2>0.007)&&(prixTransfo*1.2 <= 0.008)) ? prixTransfo*1.2 : 0.007);
			
		}
		else{
			 return ( (prixTransfo*1.2>0.007)&&(prixTransfo*1.5 <= 0.008) ? prixTransfo*1.5 : 0.007);
		}
	}
	
	public Tresorerie getFonds(){
		return this.fonds;
	}
	
	public void notif(Vente vente){
		this.setVente(vente);
		this.stock.ajoutStock(1000);
		this.prixMoyen = this.prixMoyen + vente.getPrix();
		this.getFonds().paiement(vente.getPrix()*vente.getQuantite());
		this.fondsI.setValeur(this, this.fonds.getTreso()-vente.getPrix()*vente.getQuantite());
		this.stockI.setValeur(this, this.stock.totalStock());
		
		journal.ajouter("Opération Réalisée "+vente.toString());
		journal.ajouter("Fonds "+fonds);

	}
	
	public void next(){
		double prixStock = this.prixMoyen/this.increment;
		this.prixMoyen = 0;
		this.incrementZero();
		this.stock.setPrix(prixStock);
		this.stock.vieillirStock();
	}
	
	public String getNom(){
		return "Distributeur Europe";
	}

	
	public double getPrixClient(){
		return this.stock.getPrixVente()*1.2;
	}
	
	public double getMisEnVente(){//A Compléter
		return 1000;
	}

	@Override
	public void notifVente(Vente vente) {
		// TODO Auto-generated method stub
		this.stock.retraitStock(vente.getQuantite());
		this.stockI.setValeur(this, this.stockI.getValeur()+vente.getQuantite());
		this.getFonds().encaissement(vente.getPrix()*vente.getQuantite());
		this.fondsI.setValeur(this, this.fondsI.getValeur()+vente.getPrix()*vente.getQuantite());
		
	}

	


}
