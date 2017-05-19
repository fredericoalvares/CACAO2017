package abstraction.producteur.cotedivoire;

import abstraction.fourni.Acteur;
import abstraction.fourni.Indicateur;
import abstraction.fourni.Journal;
import abstraction.fourni.Monde;
import abstraction.producteur.ameriquelatine.IProducteur;



// by fcadre, comments by antoineroson

public class ProductionCoteDIvoire implements Acteur, IProducteur{
	public static final int  productionmoyenne = 1650000/26; // Production moyenne de la cote d'ivoire en tonnes
	private int  production; //Liste des productions par périodes
	private Stock stock;          // Représente notre stock 
	private Treso tresorerie;     // Représente notre trésorerie
	private Indicateur productionIndicateur;	// Indicateur de production 
	private Indicateur stockIndicateur;		// Indicateur du stock
	private Indicateur tresoIndicateur;		// Indicateur de tréso
	private Indicateur vente;		// Indicateur de vente
	private Journal journal;		// Journal de Production Côte d'Ivoire
	
	//Cf Marché
	public int hashCode() {
		return this.getNom().hashCode();
	}
	
	//Constructeur Production Côte d'Ivoire
	public ProductionCoteDIvoire(int prods, Stock stock, Treso treso){ 
		this.production = prods; 
		this.stock=stock;
		this.tresorerie = treso; 
	}
	
	public ProductionCoteDIvoire() {
		this.production = 0;		//Initialisation de la production
		this.stock= new Stock(0);		//Initialisation du Stock
		this.tresorerie= new Treso();		//Initialisation de la trésorerie
		
		//Création des indicateurs Production Côte d'Ivoire
		this.productionIndicateur=new Indicateur("6_PROD_COT_production",this,0.0);
		Monde.LE_MONDE.ajouterIndicateur( this.productionIndicateur );
		this.stockIndicateur = new Indicateur("6_PROD_COT_stock",this,0.0);
		Monde.LE_MONDE.ajouterIndicateur(this.stockIndicateur);
		this.tresoIndicateur = new Indicateur("6_PROD_COT_treso",this,0.0);
		Monde.LE_MONDE.ajouterIndicateur(this.tresoIndicateur);
		this.vente= new Indicateur("6_PROD_COT_vente",this,0.0);
		Monde.LE_MONDE.ajouterIndicateur(this.vente);
		
		//Création du Journal Production Côte d'Ivoire
		this.journal = new Journal("Journal de "+ getNom());
		Monde.LE_MONDE.ajouterJournal(this.journal);
		
	}
	
	//Accesseur Production moyenne
	public int getProductionmoyenne(){ 
		return productionmoyenne; 
	}
	
	//Accesseur quantité produite
	public int getQuantiteProd(){ 
		return this.production;   
		// Récupére la dernière production sur la période
	}

	// Méthode varitation random de la production
	public void variationProduction(){
		double variation = 0.10;  //Variation de +- 10% 
		//Création d'une enveloppe (prod_min->prod_max)
		double prod_min = this.getProductionmoyenne() - (double)(this.getProductionmoyenne()*variation); 
		double prod_max = this.getProductionmoyenne() + (double)(this.getProductionmoyenne()*variation);
		double prod = prod_min + (double)Math.random()*(prod_max - prod_min); // Production random entre prod_min et prod_max
		this.production=(int)prod; // ajout dans la liste de production
		this.stock.addStock((int)prod); //Ajout dans le stock
		this.productionIndicateur.setValeur(this, (int)prod); //Ajout de la production dans l'indicateur de production
		this.journal.ajouter(" Valeur Produite: "+ (int) prod +"à l'étape du Monde: "+Monde.LE_MONDE.getStep()); // Ajout de la production dans le Journal
	}
	
	//Accesseur Nom
	public String getNom() {
		return "Production Cote d'Ivoire"; 
	}
	
	//Quantité mise en vente
	public double quantiteMiseEnvente() {   
		return this.stock.getStock(); 
	}

	//Notification Vente
	public void notificationVente(double quantite, double coursActuel) {	// grace a la notification de vente on met a jour // 
		this.vente.setValeur(this,quantite);
		this.stock.addStock(-quantite);
		this.tresorerie.addBenef(quantite*coursActuel - this.stock.getStock()*tresorerie.couts);   
	}
	
	//NEXT "Centre du programme -> Passage à la période suivante" 
	public void next() {
		this.variationProduction();
		this.stockIndicateur.setValeur(this,this.stock.getStock());
		this.tresoIndicateur.setValeur(this,this.tresorerie.getCa());
	}
}
