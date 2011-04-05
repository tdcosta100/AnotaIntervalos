package com.tiago.AnotaIntervalos;

import java.util.ArrayList;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class IntervalosDbAdapter
{
    private static final String TAG = "IntervalosDbAdapter";
	private final Context mContext;
	
	private DatabaseHelper mDbHelper;
	private SQLiteDatabase mDb;
	
	// estrutura do banco de dados
    private static final String DATABASE_NAME = "dadosIntervalos";
    private static final int DATABASE_VERSION = 3;
	
    public static final String TABELA_ANOTACOES 	= "anotacoes";
    public static final String ANOTACOES_IDANOTACAO = "idAnotacao";
    public static final String ANOTACOES_DATAHORA 	= "dataHora";
    public static final String ANOTACOES_LINHA 		= "linha";
    public static final String ANOTACOES_ESTACAO 	= "estacao";
    public static final String ANOTACOES_SENTIDO 	= "sentido";

    public static final String TABELA_PARTIDAS 		= "partidas";
    public static final String PARTIDAS_IDPARTIDA 	= "idPartida";
    public static final String PARTIDAS_IDANOTACAO 	= "idAnotacao";
    public static final String PARTIDAS_HORA 		= "hora";
    public static final String PARTIDAS_IDTREM 		= "idTrem";
    
    public static final String TABELA_LINHAS 		= "linhas";
    public static final String LINHAS_IDLINHA 		= "idLinha";
    public static final String LINHAS_DESCRICAO 	= "descricao";
    
    public static final String TABELA_ESTACOES 		= "estacoes";
    public static final String ESTACOES_IDESTACAO 	= "idEstacao";
    public static final String ESTACOES_IDLINHA 	= "idLinha";
    public static final String ESTACOES_SIGLA 		= "sigla";
    public static final String ESTACOES_DESCRICAO 	= "descricao";
    public static final String ESTACOES_TERMINAL 	= "terminal";
    
    private static final String DATABASE_CREATE_ANOTACOES =
        "CREATE TABLE " + TABELA_ANOTACOES + " (" +
    		ANOTACOES_IDANOTACAO	+ " INTEGER PRIMARY KEY AUTOINCREMENT, " +
    		ANOTACOES_DATAHORA 		+ " INTEGER, " +
    		ANOTACOES_LINHA 		+ " INTEGER, " +
    		ANOTACOES_ESTACAO 		+ " TEXT, " +
    		ANOTACOES_SENTIDO 		+ " TEXT);";
    
    private static final String DATABASE_CREATE_PARTIDAS =
    	"CREATE TABLE " + TABELA_PARTIDAS + " (" +
    		PARTIDAS_IDPARTIDA 	+ " INTEGER PRIMARY KEY AUTOINCREMENT, " +
    		PARTIDAS_IDANOTACAO + " INTEGER, " +
    		PARTIDAS_HORA 		+ " INTEGER, " +
    		PARTIDAS_IDTREM 	+ " TEXT);";
    
    private static final String DATABASE_CREATE_LINHAS =
    	"CREATE TABLE " + TABELA_LINHAS + " (" +
    		LINHAS_IDLINHA 		+ " INTEGER PRIMARY KEY, " +
    		LINHAS_DESCRICAO	+ " TEXT);";
	
    private static final String DATABASE_CREATE_ESTACOES =
    	"CREATE TABLE " + TABELA_ESTACOES + " (" +
    		ESTACOES_IDESTACAO 	+ " INTEGER PRIMARY KEY, " +
    		ESTACOES_IDLINHA 	+ " INTEGER, " +
    		ESTACOES_SIGLA 		+ " TEXT, " +
    		ESTACOES_DESCRICAO	+ " TEXT, " +
    		ESTACOES_TERMINAL	+ " INTEGER);";
    
    private static class DatabaseHelper extends SQLiteOpenHelper
	{
		DatabaseHelper(Context context)
		{
			super(context, DATABASE_NAME, null, DATABASE_VERSION);
		}
		
		@Override
		public void onCreate(SQLiteDatabase db)
		{
			db.execSQL(DATABASE_CREATE_ANOTACOES);
			db.execSQL(DATABASE_CREATE_PARTIDAS);
			db.execSQL(DATABASE_CREATE_LINHAS);
			db.execSQL(DATABASE_CREATE_ESTACOES);
			
			cadastrarLinhas(db);
			cadastrarEstacoes(db);
		}
		
		@Override
		public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion)
		{
			Log.w(TAG, "Upgrading database from version " + oldVersion + " to "
					+ newVersion + ", which will destroy all old data");
			db.execSQL("DROP TABLE IF EXISTS " + TABELA_ANOTACOES + ";" +
					   "DROP TABLE IF EXISTS " + TABELA_PARTIDAS);
			onCreate(db);
		}
		
		
		private void cadastrarLinhas(SQLiteDatabase db)
		{
			String[] linhas = new String[] {
				"1",
				"2",
				"3",
				"4",
				"5",
				"7",
				"8",
				"9",
				"10",
				"11",
				"12"
			};
			
			String[] descricoes = new String[] {
					"Azul",
					"Verde",
					"Vermelha",
					"Amarela",
					"Lilás",
					"Rubi",
					"Diamante",
					"Esmeralda",
					"Turquesa",
					"Coral",
					"Safira"
				};
			
			for(int i=0; i < linhas.length; i++)
			{
				ContentValues tupla = new ContentValues();
				
				tupla.put(LINHAS_IDLINHA, linhas[i]);
				tupla.put(LINHAS_DESCRICAO, linhas[i] + "-" + descricoes[i]);
				
				db.insert(TABELA_LINHAS, null, tupla);
			}
		}
		
		
		
		private void cadastrarEstacoes(SQLiteDatabase db)
		{
			ArrayList<String> tempSiglas;
			ArrayList<String> tempEstacoes;
			ArrayList<String> tempTerminais;
			
			ArrayList<String> linhas = new ArrayList<String>();
			ArrayList<ArrayList<String>> siglas = new ArrayList<ArrayList<String>>();
			ArrayList<ArrayList<String>> estacoes = new ArrayList<ArrayList<String>>();
			ArrayList<ArrayList<String>> terminais = new ArrayList<ArrayList<String>>();
			
			
			//linhas para correspondência dos arrays de siglas e estações
			linhas.add("1");
			linhas.add("2");
			linhas.add("3");
			linhas.add("4");
			linhas.add("5");
			linhas.add("7");
			linhas.add("8");
			linhas.add("9");
			linhas.add("10");
			linhas.add("11");
			linhas.add("12");
			
			
			
			//Estações da Linha 1
			tempSiglas = new ArrayList<String>(); tempEstacoes = new ArrayList<String>();
			tempSiglas.add("JAB"); tempEstacoes.add("Jabaquara");
			tempSiglas.add("CON"); tempEstacoes.add("Conceição");
			tempSiglas.add("JUD"); tempEstacoes.add("São Judas");
			tempSiglas.add("SAU"); tempEstacoes.add("Saúde");
			tempSiglas.add("ARV"); tempEstacoes.add("Praça da Árvore");
			tempSiglas.add("SCZ"); tempEstacoes.add("Santa Cruz");
			tempSiglas.add("VMN"); tempEstacoes.add("Vila Mariana");
			tempSiglas.add("ANR"); tempEstacoes.add("Ana Rosa");
			tempSiglas.add("PSO"); tempEstacoes.add("Paraíso");
			tempSiglas.add("VGO"); tempEstacoes.add("Vergueiro");
			tempSiglas.add("JQM"); tempEstacoes.add("São Joaquim");
			tempSiglas.add("LIB"); tempEstacoes.add("Liberdade");
			tempSiglas.add("PSE"); tempEstacoes.add("Sé");
			tempSiglas.add("BTO"); tempEstacoes.add("São Bento");
			tempSiglas.add("LUZ"); tempEstacoes.add("Luz");
			tempSiglas.add("TRD"); tempEstacoes.add("Tiradentes");
			tempSiglas.add("PPQ"); tempEstacoes.add("Armênia");
			tempSiglas.add("TTE"); tempEstacoes.add("Portuguesa-Tietê");
			tempSiglas.add("CDU"); tempEstacoes.add("Carandiru");
			tempSiglas.add("SAN"); tempEstacoes.add("Santana");
			tempSiglas.add("JPA"); tempEstacoes.add("Jardim São Paulo");
			tempSiglas.add("PIG"); tempEstacoes.add("Parada Inglesa");
			tempSiglas.add("TUC"); tempEstacoes.add("Tucuruvi");
			siglas.add(tempSiglas); estacoes.add(tempEstacoes);
			
			
			
			//Estações da Linha 2
			tempSiglas = new ArrayList<String>(); tempEstacoes = new ArrayList<String>();
			tempSiglas.add("VMD"); tempEstacoes.add("Vila Madalena");
			tempSiglas.add("SUM"); tempEstacoes.add("Sumaré");
			tempSiglas.add("CLI"); tempEstacoes.add("Clínicas");
			tempSiglas.add("CNS"); tempEstacoes.add("Consolação");
			tempSiglas.add("TRI"); tempEstacoes.add("Trianon-MASP");
			tempSiglas.add("BGD"); tempEstacoes.add("Brigadeiro");
			tempSiglas.add("PSO"); tempEstacoes.add("Paraíso");
			tempSiglas.add("ANR"); tempEstacoes.add("Ana Rosa");
			tempSiglas.add("CKB"); tempEstacoes.add("Cháraca Klabin");
			tempSiglas.add("IMG"); tempEstacoes.add("Santos-Imigrantes");
			tempSiglas.add("AIP"); tempEstacoes.add("Alto do Ipiranga");
			tempSiglas.add("SAC"); tempEstacoes.add("Sacomã");
			tempSiglas.add("TTI"); tempEstacoes.add("Tamanduateí");
			tempSiglas.add("VPT"); tempEstacoes.add("Vila Prudente");
			siglas.add(tempSiglas); estacoes.add(tempEstacoes);
			
			
			
			//Estações da Linha 3
			tempSiglas = new ArrayList<String>(); tempEstacoes = new ArrayList<String>();
			tempSiglas.add("ITQ"); tempEstacoes.add("Corinthians-Itaquera");
			tempSiglas.add("ART"); tempEstacoes.add("Artur Alvim");
			tempSiglas.add("PCA"); tempEstacoes.add("Patriarca");
			tempSiglas.add("VTD"); tempEstacoes.add("Vila Matilde");
			tempSiglas.add("VPA"); tempEstacoes.add("Guilermina-Esperança");
			tempSiglas.add("PEN"); tempEstacoes.add("Penha");
			tempSiglas.add("CAR"); tempEstacoes.add("Carrão");
			tempSiglas.add("TAT"); tempEstacoes.add("Tatuapé");
			tempSiglas.add("BEL"); tempEstacoes.add("Belém");
			tempSiglas.add("BRE"); tempEstacoes.add("Bresser-Mooca");
			tempSiglas.add("BAS"); tempEstacoes.add("Brás");
			tempSiglas.add("PDS"); tempEstacoes.add("Pedro Segundo");
			tempSiglas.add("PSE"); tempEstacoes.add("Sé");
			tempSiglas.add("GBU"); tempEstacoes.add("Anhangabaú");
			tempSiglas.add("REP"); tempEstacoes.add("República");
			tempSiglas.add("CEC"); tempEstacoes.add("Santa Cecília");
			tempSiglas.add("DEO"); tempEstacoes.add("Marechal Deodoro");
			tempSiglas.add("BFU"); tempEstacoes.add("Palmeiras-Barra Funda");
			siglas.add(tempSiglas); estacoes.add(tempEstacoes);
			
			

			//Estações da Linha 4
			tempSiglas = new ArrayList<String>(); tempEstacoes = new ArrayList<String>();
			tempSiglas.add("PTA"); tempEstacoes.add("Paulista");
			tempSiglas.add("FAL"); tempEstacoes.add("Faria Lima");
			siglas.add(tempSiglas); estacoes.add(tempEstacoes);
			
			
			//Estações da Linha 5
			tempSiglas = new ArrayList<String>(); tempEstacoes = new ArrayList<String>();
			tempSiglas.add("CPR"); tempEstacoes.add("Capão Redondo");
			tempSiglas.add("CPL"); tempEstacoes.add("Campo Limpo");
			tempSiglas.add("VBE"); tempEstacoes.add("Vila das Belezas");
			tempSiglas.add("GGR"); tempEstacoes.add("Giovanni Gronchi");
			tempSiglas.add("STA"); tempEstacoes.add("Santo Amaro");
			tempSiglas.add("LTR"); tempEstacoes.add("Largo Treze");
			siglas.add(tempSiglas); estacoes.add(tempEstacoes);
			
			
			//Estações da Linha 7
			tempSiglas = new ArrayList<String>(); tempEstacoes = new ArrayList<String>();
			tempSiglas.add("LUZ"); tempEstacoes.add("Luz");
			tempSiglas.add("BFU"); tempEstacoes.add("Palmeiras-Barra Funda");
			tempSiglas.add("ABR"); tempEstacoes.add("Água Branca");
			tempSiglas.add("LPA"); tempEstacoes.add("Lapa");
			tempSiglas.add("PQR"); tempEstacoes.add("Piqueri");
			tempSiglas.add("PRT"); tempEstacoes.add("Pirituba");
			tempSiglas.add("VCL"); tempEstacoes.add("Vila Clarice");
			tempSiglas.add("JRG"); tempEstacoes.add("Jaraguá");
			tempSiglas.add("PRU"); tempEstacoes.add("Perus");
			tempSiglas.add("CAI"); tempEstacoes.add("Caieiras");
			tempSiglas.add("FDR"); tempEstacoes.add("Franco da Rocha");
			tempSiglas.add("BFI"); tempEstacoes.add("Baltazar Fidélis");
			tempSiglas.add("FMO"); tempEstacoes.add("Francisco Morato");
			tempSiglas.add("BTJ"); tempEstacoes.add("Botujuru");
			tempSiglas.add("CLP"); tempEstacoes.add("Campo Limpo Paulista");
			tempSiglas.add("VPL"); tempEstacoes.add("Várzea Paulista");
			tempSiglas.add("JUN"); tempEstacoes.add("Jundiaí");
			siglas.add(tempSiglas); estacoes.add(tempEstacoes);
			
			
			//Estações da Linha 8
			tempSiglas = new ArrayList<String>(); tempEstacoes = new ArrayList<String>();
			tempSiglas.add("JPR"); tempEstacoes.add("Júlio Prestes");
			tempSiglas.add("BFU"); tempEstacoes.add("Palmeiras-Barra Funda");
			tempSiglas.add("LAB"); tempEstacoes.add("Lapa");
			tempSiglas.add("DMO"); tempEstacoes.add("Domingos de Moraes");
			tempSiglas.add("ILE"); tempEstacoes.add("Imperatriz Leopoldina");
			tempSiglas.add("PAL"); tempEstacoes.add("Presidente Altino");
			tempSiglas.add("OSA"); tempEstacoes.add("Osasco");
			tempSiglas.add("CSA"); tempEstacoes.add("Comandante Sampaio");
			tempSiglas.add("QTU"); tempEstacoes.add("Quitaúna");
			tempSiglas.add("GMC"); tempEstacoes.add("Gal. Miguel Costa");
			tempSiglas.add("CPB"); tempEstacoes.add("Carapicuíba");
			tempSiglas.add("STE"); tempEstacoes.add("Santa Terezinha");
			tempSiglas.add("AJO"); tempEstacoes.add("Antonio João");
			tempSiglas.add("BRU"); tempEstacoes.add("Barueri");
			tempSiglas.add("JBE"); tempEstacoes.add("Jardim Belval");
			tempSiglas.add("JSI"); tempEstacoes.add("Jardim Silveira");
			tempSiglas.add("JAN"); tempEstacoes.add("Jandira");
			tempSiglas.add("SCO"); tempEstacoes.add("Sagrado Coração");
			tempSiglas.add("ECD"); tempEstacoes.add("Engenheiro Cardoso");
			tempSiglas.add("IPV"); tempEstacoes.add("Itapevi");
			siglas.add(tempSiglas); estacoes.add(tempEstacoes);
			
			
			//Estações da Linha 9
			tempSiglas = new ArrayList<String>(); tempEstacoes = new ArrayList<String>();
			tempSiglas.add("OSA"); tempEstacoes.add("Osasco");
			tempSiglas.add("PAL"); tempEstacoes.add("Presidente Altino");
			tempSiglas.add("CEA"); tempEstacoes.add("Ceasa");
			tempSiglas.add("JAG"); tempEstacoes.add("Villa Lobos-Jaguaré");
			tempSiglas.add("USP"); tempEstacoes.add("Cidade Universitária");
			tempSiglas.add("PIN"); tempEstacoes.add("Pinheiros");
			tempSiglas.add("HBR"); tempEstacoes.add("Hebraica-Rebouças");
			tempSiglas.add("CJD"); tempEstacoes.add("Cidade Jardim");
			tempSiglas.add("VOL"); tempEstacoes.add("Vila Olímpia");
			tempSiglas.add("BRR"); tempEstacoes.add("Berrini");
			tempSiglas.add("MRB"); tempEstacoes.add("Morumbi");
			tempSiglas.add("GJT"); tempEstacoes.add("Granja Julieta");
			tempSiglas.add("SAM"); tempEstacoes.add("Santo Amaro");
			tempSiglas.add("SOC"); tempEstacoes.add("Socorro");
			tempSiglas.add("JUR"); tempEstacoes.add("Jurubatuba");
			tempSiglas.add("AUT"); tempEstacoes.add("Autódromo");
			tempSiglas.add("INT"); tempEstacoes.add("Primavera-Interlagos");
			tempSiglas.add("GRA"); tempEstacoes.add("Grajaú");
			siglas.add(tempSiglas); estacoes.add(tempEstacoes);
			
			
			//Estações da Linha 10
			tempSiglas = new ArrayList<String>(); tempEstacoes = new ArrayList<String>();
			tempSiglas.add("LUZ"); tempEstacoes.add("Luz");
			tempSiglas.add("BAS"); tempEstacoes.add("Brás");
			tempSiglas.add("MOC"); tempEstacoes.add("Mooca");
			tempSiglas.add("IPG"); tempEstacoes.add("Ipiranga");
			tempSiglas.add("TMD"); tempEstacoes.add("Tamanduateí");
			tempSiglas.add("SCT"); tempEstacoes.add("São Caetano");
			tempSiglas.add("UTG"); tempEstacoes.add("Utinga");
			tempSiglas.add("PSA"); tempEstacoes.add("Prefeito Saladino");
			tempSiglas.add("SAN"); tempEstacoes.add("Santo André");
			tempSiglas.add("CPV"); tempEstacoes.add("Capuava");
			tempSiglas.add("MAU"); tempEstacoes.add("Mauá");
			tempSiglas.add("GPT"); tempEstacoes.add("Guapituba");
			tempSiglas.add("RPI"); tempEstacoes.add("Ribeirão Pires");
			tempSiglas.add("RGS"); tempEstacoes.add("Rio Grande da Serra");
			siglas.add(tempSiglas); estacoes.add(tempEstacoes);
			
			
			//Estações da Linha 11
			tempSiglas = new ArrayList<String>(); tempEstacoes = new ArrayList<String>();
			tempSiglas.add("LUZ"); tempEstacoes.add("Luz");
			tempSiglas.add("BAS"); tempEstacoes.add("Brás");
			tempSiglas.add("TAT"); tempEstacoes.add("Tatuapé");
			tempSiglas.add("ITQ"); tempEstacoes.add("Corinthians-Itaquera");
			tempSiglas.add("DBO"); tempEstacoes.add("Dom Bosco");
			tempSiglas.add("JBO"); tempEstacoes.add("José Bonifácio");
			tempSiglas.add("GUA"); tempEstacoes.add("Guaianazes");
			tempSiglas.add("AGN"); tempEstacoes.add("Antonio Gianetti Neto");
			tempSiglas.add("FVC"); tempEstacoes.add("Ferraz de Vasconcelos");
			tempSiglas.add("POA"); tempEstacoes.add("Poá");
			tempSiglas.add("CVN"); tempEstacoes.add("Calmon Viana");
			tempSiglas.add("SUZ"); tempEstacoes.add("Suzano");
			tempSiglas.add("JPB"); tempEstacoes.add("Jundiapeba");
			tempSiglas.add("BCB"); tempEstacoes.add("Brás Cubas");
			tempSiglas.add("MDC"); tempEstacoes.add("Mogi das Cruzes");
			tempSiglas.add("EST"); tempEstacoes.add("Estudantes");
			siglas.add(tempSiglas); estacoes.add(tempEstacoes);
			
			
			//Estações da Linha 12
			tempSiglas = new ArrayList<String>(); tempEstacoes = new ArrayList<String>();
			tempSiglas.add("BAS"); tempEstacoes.add("Brás");
			tempSiglas.add("TAT"); tempEstacoes.add("Tatuapé");
			tempSiglas.add("EGO"); tempEstacoes.add("Eng. Goulart");
			tempSiglas.add("USL"); tempEstacoes.add("USP Leste");
			tempSiglas.add("ERM"); tempEstacoes.add("Comendador Ermelino");
			tempSiglas.add("SMP"); tempEstacoes.add("São Miguel Paulista");
			tempSiglas.add("JHE"); tempEstacoes.add("Jardim Helena-Vila Mara");
			tempSiglas.add("ITI"); tempEstacoes.add("Itaim Paulista");
			tempSiglas.add("JRO"); tempEstacoes.add("Jardim Romano");
			tempSiglas.add("EMF"); tempEstacoes.add("Eng. Manoel Feio");
			tempSiglas.add("IQC"); tempEstacoes.add("Itaquaquecetuba");
			tempSiglas.add("ARC"); tempEstacoes.add("Aracaré");
			tempSiglas.add("CVN"); tempEstacoes.add("Calmon Viana");
			siglas.add(tempSiglas); estacoes.add(tempEstacoes);
			
			
			
			//Estações terminais
			//Linha 1
			tempTerminais = new ArrayList<String>();
			tempTerminais.add("JAB");
			tempTerminais.add("TUC");
			terminais.add(tempTerminais);
			
			
			//Linha 2
			tempTerminais = new ArrayList<String>();
			tempTerminais.add("VMD");
			tempTerminais.add("SAC");
			tempTerminais.add("VPT");
			terminais.add(tempTerminais);
			
			
			//Linha 3
			tempTerminais = new ArrayList<String>();
			tempTerminais.add("ITQ");
			tempTerminais.add("BFU");
			terminais.add(tempTerminais);
			
			
			//Linha 4
			tempTerminais = new ArrayList<String>();
			tempTerminais.add("PTA");
			tempTerminais.add("FAL");
			terminais.add(tempTerminais);
			
			
			//Linha 5
			tempTerminais = new ArrayList<String>();
			tempTerminais.add("CPR");
			tempTerminais.add("LTR");
			terminais.add(tempTerminais);
			
			
			//Linha 7
			tempTerminais = new ArrayList<String>();
			tempTerminais.add("LUZ");
			tempTerminais.add("FMO");
			tempTerminais.add("JUN");
			terminais.add(tempTerminais);
			
			
			//Linha 8
			tempTerminais = new ArrayList<String>();
			tempTerminais.add("JPR");
			tempTerminais.add("IPV");
			terminais.add(tempTerminais);
			
			
			//Linha 9
			tempTerminais = new ArrayList<String>();
			tempTerminais.add("OSA");
			tempTerminais.add("GRA");
			terminais.add(tempTerminais);
			
			
			//Linha 10
			tempTerminais = new ArrayList<String>();
			tempTerminais.add("LUZ");
			tempTerminais.add("RGS");
			terminais.add(tempTerminais);
			
			
			//Linha 11
			tempTerminais = new ArrayList<String>();
			tempTerminais.add("LUZ");
			tempTerminais.add("GUA");
			tempTerminais.add("EST");
			terminais.add(tempTerminais);
			
			
			//Linha 12
			tempTerminais = new ArrayList<String>();
			tempTerminais.add("BAS");
			tempTerminais.add("CVN");
			terminais.add(tempTerminais);
			
			
			for(int i=0; i < linhas.size(); i++)
			{
				for(int j=0; j < estacoes.get(i).size(); j++)
				{
					ContentValues tupla = new ContentValues();
					
					tupla.put(ESTACOES_IDLINHA, linhas.get(i));
					tupla.put(ESTACOES_SIGLA, siglas.get(i).get(j));
					tupla.put(ESTACOES_DESCRICAO, estacoes.get(i).get(j));
					tupla.put(ESTACOES_TERMINAL, terminais.get(i).contains(siglas.get(i).get(j)));
					
					db.insert(TABELA_ESTACOES, null, tupla);
				}
			}
		}
	}
	
	public IntervalosDbAdapter(Context context)
	{
		mContext = context;
	}
	
	/**
	 * Open the notes database. If it cannot be opened, try to create a new
	 * instance of the database. If it cannot be created, throw an exception to
	 * signal the failure
	 * 
	 * @return this (self reference, allowing this to be chained in an
	 *         initialization call)
	 * @throws SQLException
	 *             if the database could be neither opened or created
	 */
	public IntervalosDbAdapter open() throws SQLException
	{
		mDbHelper = new DatabaseHelper(mContext);
		mDb = mDbHelper.getWritableDatabase();
		return this;
	}
	
	public void close()
	{
		mDbHelper.close();
	}
	
	
	/**
	 * Cria uma nova anotação de intervalos. Se for criada 
	 * com sucesso, retorna a idAnotacao recém-criada. Caso contrário, retorna
	 * -1 para indicar falha.
	 * 
	 * @param dataHora
	 *            data e hora da criação da anotação. Deve estar no formato <i>YYYY-MM-DD HH:MM:SS</i>
	 * @param linha
	 *            a linha a ser anotada (1 dígito numérico. Linha 1-Azul deve ser armazenada como "1"
	 * @param estacao
	 *            dígito de 3 letras indicando a estação onde será realizada a anotação
	 * @param sentido
	 *            dígito de 3 letras indicando a estação terminal do sentido a ter o intervalo anotado
	 * @return idAnotacao ou -1 em caso de falha
	 */
	public long insertAnotacao(long dataHora, String linha, String estacao, String sentido)
	{
		ContentValues initialValues = new ContentValues();
		initialValues.put(ANOTACOES_DATAHORA, dataHora);
		initialValues.put(ANOTACOES_LINHA, linha);
		initialValues.put(ANOTACOES_ESTACAO, estacao);
		initialValues.put(ANOTACOES_SENTIDO, sentido);
		
		return mDb.insert(TABELA_ANOTACOES, null, initialValues);
	}
	
	
	
	/**
	 * Atualiza a anotação com os dados fornecidos. A anotação a ser atualizada é
	 * especificada através da idAnotacao, e é atualizada através dos valores fornecidos
	 * pelos parâmetros
	 * 
	 * @param idAnotacao
	 *            id da anotação a ser atualizada
	 * @param dataHora
	 *            data e hora da criação da anotação. Deve estar no formato <i>YYYY-MM-DD HH:MM:SS</i>
	 * @param linha
	 *            a linha a ser anotada (1 dígito numérico. Linha 1-Azul deve ser armazenada como "1"
	 * @param estacao
	 *            dígito de 3 letras indicando a estação onde será realizada a anotação
	 * @param sentido
	 *            dígito de 3 letras indicando a estação terminal do sentido a ter o intervalo anotado
	 * @return true se a anotação for atualizada, false em caso contrário
	 */
	public boolean updateAnotacao(long idAnotacao, long dataHora, String linha, String estacao, String sentido)
	{
		ContentValues args = new ContentValues();
		args.put(ANOTACOES_DATAHORA, dataHora);
		args.put(ANOTACOES_LINHA, linha);
		args.put(ANOTACOES_ESTACAO, estacao);
		args.put(ANOTACOES_SENTIDO, sentido);
		
		return mDb.update(TABELA_ANOTACOES, args, ANOTACOES_IDANOTACAO + " = " + idAnotacao, null) > 0;
	}
	
	
	
	/**
	 * Remove a anotação com o idAnotacao fornecido
	 * 
	 * @param idAnotacao
	 *            id da anotação a ser removida
	 * @return true se removida, false em caso contrário
	 */
	public boolean deleteAnotacao(long idAnotacao)
	{
		boolean resultado = mDb.delete(TABELA_ANOTACOES, ANOTACOES_IDANOTACAO + " = " + idAnotacao, null) > 0;
		
		//excluir também as partidas relacionadas à anotação
		return mDb.delete(TABELA_PARTIDAS, ANOTACOES_IDANOTACAO + " = " + idAnotacao, null) > 0 && resultado;
	}
	
	
	
	/**
	 * Retorna um Cursor posicionado na anotação que corresponde à idAnotacao
	 * 
	 * @param idAnotacao
	 *            id da anotação a ser obtida
	 * @return Cursor posicionado na anotação correspondente, se existente
	 * @throws SQLException
	 *             se a anotação não puder ser encontrada/obtida
	 */
	public Cursor fetchAnotacao(long idAnotacao) throws SQLException
	{
		Cursor mCursor = mDb.query(true, TABELA_ANOTACOES,
				new String[] {
				ANOTACOES_IDANOTACAO,
				ANOTACOES_DATAHORA,
				ANOTACOES_LINHA,
				ANOTACOES_ESTACAO,
				ANOTACOES_SENTIDO },
				ANOTACOES_IDANOTACAO + " = '" + idAnotacao + "'", null, null, null, null, null);
		
		if (mCursor != null)
		{
			mCursor.moveToFirst();
		}
		
		return mCursor;
	}
	
	
	
	/**
	 * Retorna um Cursor com a lista de todas as anotações do banco de dados
	 * 
	 * @return Cursor com todas as anotações
	 */
	public Cursor fetchAllAnotacoes()
	{
		
		return mDb.query(TABELA_ANOTACOES,
				new String[] {
				ANOTACOES_IDANOTACAO + " AS _id",
				ANOTACOES_IDANOTACAO,
				//"strftime('%d/%m/%Y %H:%M:%S', " + ANOTACOES_DATAHORA + ", 'unixepoch', 'localtime') AS " + ANOTACOES_DATAHORA,
				ANOTACOES_DATAHORA,
				//"'Linha ' || " + ANOTACOES_LINHA + " AS " + ANOTACOES_LINHA,
				ANOTACOES_LINHA,
				ANOTACOES_ESTACAO,
				ANOTACOES_SENTIDO,
				ANOTACOES_DATAHORA + " AS ordenacao" }, null, null, null, null, "ordenacao ASC");
	}
	
	
	
	/**
	 * Cria uma nova partida de trem. Se for criada 
	 * com sucesso, retorna a idPartida recém-criada. Caso contrário, retorna
	 * -1 para indicar falha.
	 * 
	 * @param idAnotacao
	 *            id da anotação à qual a partida se refere
	 * @param hora
	 *            horário da partida. Deve estar no formato de timestamp (sem milisegundos)
	 * @param idTrem
	 *            identificação do trem que está partindo
	 * @return idPartida ou -1 em caso de falha
	 */
	public long insertPartida(long idAnotacao, long hora, String idTrem)
	{
		ContentValues initialValues = new ContentValues();
		initialValues.put(PARTIDAS_IDANOTACAO, idAnotacao);
		initialValues.put(PARTIDAS_HORA, hora);
		initialValues.put(PARTIDAS_IDTREM, idTrem);
		
		return mDb.insert(TABELA_PARTIDAS, null, initialValues);
	}
	
	
	
	/**
	 * Atualiza a partida com os dados fornecidos. A partida a ser atualizada é
	 * especificada através da idPartida, e é atualizada através dos valores fornecidos
	 * pelos parâmetros
	 * 
	 * @param idPartida
	 *            id da partida a ser atualizada
	 * @param idAnotacao
	 *            id da anotação à qual a partida se refere
	 * @param hora
	 *            horário da partida. Deve estar no formato de timestamp (sem milisegundos)
	 * @param idTrem
	 *            identificação do trem que está partindo
	 * @return true se a partida for atualizada, false em caso contrário
	 */
	public boolean updatePartida(long idPartida, long idAnotacao, long hora, String idTrem)
	{
		ContentValues args = new ContentValues();
		args.put(PARTIDAS_IDANOTACAO, idAnotacao);
		args.put(PARTIDAS_HORA, hora);
		args.put(PARTIDAS_IDTREM, idTrem);
		
		return mDb.update(TABELA_PARTIDAS, args, PARTIDAS_IDPARTIDA + " = " + idPartida, null) > 0;
	}
	
	
	
	/**
	 * Remove a partida com o idPartida fornecido
	 * 
	 * @param idPartida
	 *            id da partida a ser removida
	 * @return true se removida, false em caso contrário
	 */
	public boolean deletePartida(long idPartida)
	{
		return mDb.delete(TABELA_PARTIDAS, PARTIDAS_IDPARTIDA + " = " + idPartida, null) > 0;
	}
	
	
	
	/**
	 * Retorna um Cursor posicionado na partida que corresponde à idPartida
	 * 
	 * @param idPartida
	 *            id da partida a ser obtida
	 * @return Cursor posicionado na partida correspondente, se existente
	 * @throws SQLException
	 *             se a partida não puder ser encontrada/obtida
	 */
	public Cursor fetchPartida(long idPartida) throws SQLException
	{
		Cursor mCursor = mDb.query(true, TABELA_PARTIDAS,
				new String[] {
				PARTIDAS_IDPARTIDA,
				PARTIDAS_IDANOTACAO,
				PARTIDAS_HORA,
				PARTIDAS_IDTREM },
				PARTIDAS_IDPARTIDA + " = '" + idPartida + "'", null, null, null, null, null);
		
		if (mCursor != null)
		{
			mCursor.moveToFirst();
		}
		
		return mCursor;
	}
	
	
	
	/**
	 * Retorna um Cursor com a lista de todas as partidas do banco de dados
	 * que são relacionadas à idAnotacao
	 * 
	 * @param idAnotacao
	 *            id da anotação a ter as partidas relacionadas obtidas
	 * @return Cursor com todas as partidas relacionadas à idAnotacao
	 */
	public Cursor fetchAllPartidas(long idAnotacao)
	{
		
		return mDb.query(TABELA_PARTIDAS,
				new String[] {
				PARTIDAS_IDPARTIDA + " AS _id",
				PARTIDAS_IDPARTIDA,
				PARTIDAS_IDANOTACAO,
				PARTIDAS_HORA,
				PARTIDAS_IDTREM }, PARTIDAS_IDANOTACAO + " = '" + idAnotacao + "'", null, null, null, PARTIDAS_HORA + " ASC");
	}
	
	
	
	/**
	 * Retorna um Cursor posicionado na linha que corresponde à idLinha
	 * 
	 * @param idLinha
	 *            id da linha a ser obtida
	 * @return Cursor posicionado na linha correspondente, se existente
	 * @throws SQLException
	 *             se a linha não puder ser encontrada/obtida
	 */
	public Cursor fetchLinha(long idLinha) throws SQLException
	{
		Cursor mCursor = mDb.query(true, TABELA_LINHAS,
				new String[] {
				LINHAS_IDLINHA,
				LINHAS_DESCRICAO },
				LINHAS_IDLINHA + " = '" + idLinha + "'", null, null, null, null, null);
		
		if (mCursor != null)
		{
			mCursor.moveToFirst();
		}
		
		return mCursor;
	}
	
	
	
	/**
	 * Retorna um Cursor com a lista de todas as linhas do banco de dados
	 * 
	 * @return Cursor com todas as linhas
	 */
	public Cursor fetchAllLinhas()
	{
		
		return mDb.query(TABELA_LINHAS,
				new String[] {
				LINHAS_IDLINHA + " AS _id",
				LINHAS_IDLINHA,
				LINHAS_DESCRICAO }, null, null, null, null, LINHAS_IDLINHA + " ASC");
	}
	
	
	
	/**
	 * Retorna um Cursor posicionado na estação que corresponde à idEstacao
	 * 
	 * @param idEstacao
	 *            id da estação a ser obtida
	 * @return Cursor posicionado na estação correspondente, se existente
	 * @throws SQLException
	 *             se a estação não puder ser encontrada/obtida
	 */
	public Cursor fetchEstacao(long idEstacao) throws SQLException
	{
		Cursor mCursor = mDb.query(true, TABELA_ESTACOES,
				new String[] {
				ESTACOES_IDESTACAO,
				ESTACOES_IDLINHA,
				ESTACOES_SIGLA,
				ESTACOES_DESCRICAO,
				ESTACOES_TERMINAL },
				ESTACOES_IDESTACAO + " = '" + idEstacao + "'", null, null, null, null, null);
		
		if (mCursor != null)
		{
			mCursor.moveToFirst();
		}
		
		return mCursor;
	}
	
	
	
	/**
	 * Retorna um Cursor posicionado na estação que corresponde à sigla
	 * 
	 * @param sigla
	 *            id da estação a ser obtida
	 * @param idLinha
	 *            id da linha. Necessário por existirem linhas duplicadas
	 * @return Cursor posicionado na estação correspondente, se existente
	 * @throws SQLException
	 *             se a estação não puder ser encontrada/obtida
	 */
	public Cursor fetchEstacao(String sigla, long idLinha) throws SQLException
	{
		Cursor mCursor = mDb.query(true, TABELA_ESTACOES,
				new String[] {
				ESTACOES_IDESTACAO,
				ESTACOES_IDLINHA,
				ESTACOES_SIGLA,
				ESTACOES_DESCRICAO,
				ESTACOES_TERMINAL },
				ESTACOES_SIGLA + " = '" + sigla + "' AND " + ESTACOES_IDLINHA + " = '" + idLinha + "'",
				null, null, null, null, null);
		
		if (mCursor != null)
		{
			mCursor.moveToFirst();
		}
		
		return mCursor;
	}
	
	
	
	/**
	 * Retorna um Cursor com a lista de todas as estações relacionadas à linha idLinha
	 * 
	 * @param idLinha
	 *            id da linha
	 * @return Cursor com todas as estações relacionadas de uma linha
	 */
	public Cursor fetchAllEstacoes(long idLinha)
	{
		
		return mDb.query(TABELA_ESTACOES,
				new String[] {
				ESTACOES_IDESTACAO + " AS _id",
				ESTACOES_IDESTACAO,
				ESTACOES_IDLINHA,
				ESTACOES_SIGLA + " ||' - '|| " + ESTACOES_DESCRICAO + " AS " + ESTACOES_DESCRICAO,
				ESTACOES_TERMINAL }, ESTACOES_IDLINHA + " = '" + idLinha + "'", null, null, null, ESTACOES_IDESTACAO + " ASC");
	}
	
	
	
	/**
	 * Retorna um Cursor com a lista de todas estações terminais da linha idLinha
	 * 
	 * @param idLinha
	 *            id da linha
	 * @return Cursor com todas as estações terminais da linha
	 */
	public Cursor fetchAllEstacoesTerminais(long idLinha)
	{
		
		return mDb.query(TABELA_ESTACOES,
				new String[] {
				ESTACOES_IDESTACAO + " AS _id",
				ESTACOES_IDESTACAO,
				ESTACOES_IDLINHA,
				ESTACOES_SIGLA + " ||' - '|| " + ESTACOES_DESCRICAO + " AS descricao",
				ESTACOES_TERMINAL }, ESTACOES_IDLINHA + " = '" + idLinha + "' AND " + ESTACOES_TERMINAL + " = '1'", null, null, null, ESTACOES_IDESTACAO + " ASC");
	}

}
