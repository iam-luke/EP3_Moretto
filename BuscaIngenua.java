import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;

public class BuscaIngenua {
	
	protected final int MAX = 100000000;
	protected int[] arranjo;
	int size = 0;
	
	BuscaIngenua () { arranjo = new int[MAX]; }
	
	void insert(int s) {
		arranjo [size] = s;
		size++;
	}
	
	boolean find(int a) {
		for (int i = 0; i < size; i++) {
			if (arranjo[i] == a) return true;
		}
		return false;
	}
	
	public static BufferedReader leitor(String path) {
		try {
			FileInputStream fstream = new FileInputStream(path);
			BufferedReader br = new BufferedReader(new InputStreamReader(fstream));
			return br;
		} catch (Exception e) {
			System.out.println("Erro ao ler arquivo " + path + " : " + e.getMessage());
			return null;
		}
	}
	
	public static BufferedWriter escritor(String path) {
		try {
			FileOutputStream fstream = new FileOutputStream(path);
			BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(fstream));
			return bw;
		} catch (Exception e) {
			System.out.println("Erro ao escrever no arquivo " + path + " : " + e.getMessage());
			return null;
		}
	}
	
	public static void preencherDicionario(BuscaIngenua buscaIng, String dir) throws IOException {
		BufferedReader leitor = null;
		try {
			File directoryPath = new File(dir);
			String contents[] = directoryPath.list(); // Lista com todos os arquivos existentes na pasta de entrada
			String str;
			
			for (int i = 0; i < contents.length; i++) { // Percorre os arquivos de preenchimento
				System.out.println("Preenchendo dicionario com dados do Arquivo: " + contents[i]);
				
				leitor = leitor(dir + "\\" + contents[i]);
				str = leitor.readLine();
				
				while (str != null) { // Percorre as linhas do arquivo de preenchimento
					str = str.trim(); // Remove os espacos antes e depois do numero
					if (!str.equals("")) {
						buscaIng.insert(Integer.parseInt(str));
					}
					str = leitor.readLine();
				}
				leitor.close();
			}
		} catch (ArrayIndexOutOfBoundsException e) {
			System.out.println();
			System.err.println("Erro ao executar o programa - A quantidade de chaves inseridas ultrapassa o limite de 100000000!");
			if (leitor != null) leitor.close();
			System.exit(0);
		} catch (Exception e) {
			System.err.println("Erro ao executar o programa (Possivelmente Arquivo de Entrada Corrompido): " + e.getMessage());
			e.printStackTrace();
			if (leitor != null) leitor.close();
			System.exit(0);
		}
	}
	
	public static void main(String[] args) {
		try {
			String dirArqLog = "C:\\Users\\PC\\Desktop\\EP3\\log3.txt";					// Insira aqui o caminho do ARQUIVO que recebera o log
			String dirArqTempo = "C:\\Users\\PC\\Desktop\\EP3\\tempo3.csv";				// Insira aqui o caminho do ARQUIVO que recebera o tempo de processamento
			String dirPastaEntradas = "C:\\Users\\PC\\Desktop\\EP3\\Entrada";			// Insira aqui o caminho da PASTA que contem os arquivos de entrada
			String dirPastaVerificacoes = "C:\\Users\\PC\\Desktop\\EP3\\Verificacao";	// Insira aqui o caminho da PASTA que contem os arquivos de verificacao

			BuscaIngenua buscaIng = new BuscaIngenua();
			preencherDicionario(buscaIng, dirPastaEntradas);
			String str;
			
			BufferedWriter log = escritor(dirArqLog);
			BufferedWriter tempo = escritor(dirArqTempo);
			tempo.write("Nome do Arquivo;Tempo de Processamento(ms);Quantidade de Linhas"); // Cabecalho do arquivo dos tempos de processamento
			tempo.newLine();
			
			File directoryPath = new File(dirPastaVerificacoes);
			String contents[] = directoryPath.list(); // Lista com todos os arquivos existentes na pasta de verificacao
			BufferedReader leitor;
			
			for (int i = 0; i < contents.length; i++) { // Percorre os arquivos da pasta de verificacao
				System.out.println("Verificando Arquivo: " + contents[i]);
				log.write("Verificando Arquivo: " + contents[i]);
				log.newLine();
				log.newLine();
				
				leitor = leitor(dirPastaVerificacoes + "\\" + contents[i]);
				str = leitor.readLine();
				int qntLinhas = 0;
				long t = System.currentTimeMillis();
				
				while (str != null) { // Percorre as linhas do arquivo de verificacao
					str = str.trim(); // Remove os espacos antes e depois do numero
					qntLinhas++;
					
					if (!str.equals("")) {
						if (buscaIng.find(Integer.parseInt(str))) {
							log.write("Chave " + str + "\t encontrada!");
						} else {
							log.write("Chave " + str + "\t nao encontrada...");
						}
						log.newLine();
					}
					str = leitor.readLine();
				}
				t = System.currentTimeMillis() - t;
				
				tempo.write(contents[i] + ";" + t + ";" + qntLinhas);
				tempo.newLine();
				if (i != contents.length - 1) log.newLine();
				leitor.close();
			}
			log.close();
			tempo.close();
			
		} catch (Exception e) {
			System.out.println("Erro ao executar o programa (Possivelmente Arquivo de Verificacao Corrompido): " + e.getMessage());
			e.printStackTrace();
		}
	}
}
