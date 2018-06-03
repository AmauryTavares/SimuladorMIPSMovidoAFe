import java.io.IOException;

import br.ufrpe.deinfo.aoc.mips.MIPS;
import br.ufrpe.deinfo.aoc.mips.Simulator;
import br.ufrpe.deinfo.aoc.mips.State;
import jline.console.ConsoleReader;

public class MyMIPS implements MIPS {

	@SuppressWarnings("unused")
	private ConsoleReader console;
	
	public MyMIPS() throws IOException {
		this.console = Simulator.getConsole();
	}
	
	static boolean PC4 = true;
	
	@Override
	public void execute(State state) throws Exception {
		
		Integer PC = state.getPC();
		PC4 = true;
		
		if (PC.equals(0)) {
			state.writeRegister(28, 0x1800);
			state.writeRegister(29, 0x3ffc);
		}
		
		String instrucaoAtual =  completeToLeft(Integer.toBinaryString(state.readInstructionMemory(PC)), '0', 32);
		String op = instrucaoAtual.substring(0, 6);
		
		if (op.equals("000000")) {
			InstTipoR(instrucaoAtual, state);
		} else if (op.equals("000011") || op.equals("000010")) {
			InstTipoJ(instrucaoAtual, state);
		} else {
			InstTipoI(instrucaoAtual, state);
		}
		
		if (PC4) {
			state.setPC(state.getPC() + 4);	
		}
		PC4 = true;
	}
	
	public static void main(String[] args) {
		try {
			Simulator.setMIPS(new MyMIPS());
			Simulator.setLogLevel(Simulator.LogLevel.INFO);
			Simulator.start();
		} catch (Exception e) {		
			e.printStackTrace();
		}		
	}

	public static String completeToLeft(String value, char c, int size) {
		if (value == null) {
			value = "";
		}
		String result = value;
		while ( result.length() < size ) {
			result = c + result;
		}
		return result;
	}
	
	
	public static String BinarioComSinal(String value, int size) { //Modificada ***
		if (value == null) {
			value = "";
		}
		
		String result = value;
		
		if (result.length() != 32)  {
			while ( result.length() < size ) {
				result = '0' + result;
			} 
		} else if (result.charAt(0) == '1'){

		/*
		if ((result.charAt(0)) == '1') {
			  while ( result.length() < size ) {
				result = '1' + result;
			}
		*/	
			String novoResult = "";
			  
			for (int i = 31; i >= 0; i--) { //Complemento a 1
				if (result.charAt(i) == '1') {
					novoResult = '0' +  novoResult;
				} else {
					novoResult = '1' +  novoResult;
				}
			}
			
			String novoResultFinal = "";
			boolean carryIn = true;
			for (int i = 31; i >= 0; i--) { //Complemento a 2
				if (novoResult.charAt(i) == '1' && carryIn == true) {
					novoResultFinal = '0' + novoResultFinal;
					carryIn = true;
				} else if (carryIn == true){
					novoResultFinal = '1' + novoResultFinal;
					carryIn = false;
				} else {
					novoResultFinal = novoResult.charAt(i) + novoResultFinal;
				}
			}
			
			result = novoResultFinal;
			result = '-' + result;
		}

		return result;
	}
	
	public static String andAuxiliar(String rgRS, String rgRT, int size) {
		if (rgRS == null) {
			rgRS = "";
		}
		
		if (rgRT == null) {
			rgRT = "";
		}		
		
		String valorRS = rgRS;
		String valorRT = rgRT;
		
		
		while ( valorRS.length() < size ) {
			valorRS = '0' + valorRS;
		}
		
		while ( valorRT.length() < size ) {
			valorRT = '0' + valorRT;
		}
		
		String resultadoFinal = "";
		
		for (int i = 31; i >= 0; i--) {
			if((valorRS.charAt(i)) == '1' && (valorRT.charAt(i)) == '1') {
				resultadoFinal = '1' + resultadoFinal;
			} else {
				resultadoFinal = '0' + resultadoFinal;
			}
		}
		
		return resultadoFinal;
		
	}
	
	public static String jrAuxiliar(String rgRS, int size) {
		if (rgRS == null) {
			rgRS = "";
		}
		
		String valorRS = rgRS;
		
			while ( valorRS.length() < size ) {
				valorRS = '0' + valorRS;
			}
			
		return valorRS;
	}
	
	public static String norAuxiliar(String rgRS, String rgRT, int size) {
		if (rgRS == null) {
			rgRS = "";
		}
		
		if (rgRT == null) {
			rgRT = "";
		}	
		
		String valorRS = rgRS;
		String valorRT = rgRT;
		
		while ( valorRS.length() < size ) {
			valorRS = '0' + valorRS;
		}
		
		while ( valorRT.length() < size ) {
			valorRT = '0' + valorRT;
		}
		
		String resultadoFinal = "";
		
		for (int i = 31; i >= 0; i--) {
			if((valorRS.charAt(i)) == '0' && (valorRT.charAt(i)) == '0') {
				resultadoFinal = '1' + resultadoFinal;
			} else {
				resultadoFinal = '0' + resultadoFinal;
			}
		}
		
		return resultadoFinal;
	}
	
	public static String orAuxiliar(String rgRS, String rgRT, int size) {
		if (rgRS == null) {
			rgRS = "";
		}
		
		if (rgRT == null) {
			rgRT = "";
		}	
		
		String valorRS = rgRS;
		String valorRT = rgRT;
		
		while ( valorRS.length() < size ) {
			valorRS = '0' + valorRS;
		}
		
		while ( valorRT.length() < size ) {
			valorRT = '0' + valorRT;
		}
		
		String resultadoFinal = "";
		
		for (int i = 31; i >= 0; i--) {
			if((valorRS.charAt(i)) == '0' && (valorRT.charAt(i)) == '0') {
				resultadoFinal = '0' + resultadoFinal;
			} else {
				resultadoFinal = '1' + resultadoFinal;
			}
		}
		
		return resultadoFinal;
	}
	
	public static void InstTipoR (String instrucaoAtual, State state) {
		
		Integer rs = Integer.parseInt((instrucaoAtual.substring(6, 11)), 2);
		Integer rt = Integer.parseInt((instrucaoAtual.substring(11, 16)), 2);
		Integer rd = Integer.parseInt((instrucaoAtual.substring(16, 21)), 2);
		Integer shamt = Integer.parseInt((instrucaoAtual.substring(21, 26)), 2);
		String funct = instrucaoAtual.substring(26, 32);
		Integer valorRS = state.readRegister(rs);
		Integer valorRT = state.readRegister(rt);
		Integer resultado = 0;
		
		switch (funct) {
		case "100000":  //FUN플O ADD
				valorRS = Integer.parseInt((BinarioComSinal(Integer.toBinaryString(valorRS), 32)), 2);
				valorRT = Integer.parseInt(BinarioComSinal(Integer.toBinaryString(valorRT), 32), 2);
				resultado = valorRS + valorRT;
				state.writeRegister(rd, resultado);
			break;
			
		case "100001": 	//FUN플O ADDU
				valorRS = Integer.parseInt(BinarioComSinal(Integer.toBinaryString(valorRS), 32), 2);
				valorRT = Integer.parseInt(BinarioComSinal(Integer.toBinaryString(valorRT), 32), 2);
				resultado = valorRS + valorRT;
				state.writeRegister(rd, resultado);
			break;
			
		case "100100": //FUN플O AND
				resultado = Integer.parseInt(BinarioComSinal(andAuxiliar(Integer.toBinaryString(valorRS), Integer.toBinaryString(valorRT), 32), 32), 2);
				state.writeRegister(rd, resultado);
			break;
			
		case "001000":	//FUN플O JR
				resultado = Integer.parseInt(jrAuxiliar(Integer.toBinaryString(valorRS), 32), 2);
				state.setPC(resultado);
				PC4 = false;
			break;
			
		case "100111":	//FUN플O NOR			
				resultado = Integer.parseInt(BinarioComSinal(norAuxiliar(Integer.toBinaryString(valorRS), Integer.toBinaryString(valorRT), 32), 32), 2);
				state.writeRegister(rd, resultado);
			break;
			
		case "100101":	//FUN플O OR
				resultado = Integer.parseInt(BinarioComSinal(orAuxiliar(Integer.toBinaryString(valorRS), Integer.toBinaryString(valorRT), 32), 32), 2); 
				state.writeRegister(rd, resultado);
			break;
			
		case "101010":	//FUN플O SLT
			
			if (valorRS < valorRT) {
				resultado = 1;
			} else {
				resultado = 0;
			}
			
			state.writeRegister(rd, resultado);
			break;
			
		case "101011":	//FUN플O SLTU
				long valorRSBig = Long.parseLong(completeToLeft(Integer.toBinaryString(valorRS), '0', 32), 2);
				long valorRTBig = Long.parseLong(completeToLeft(Integer.toBinaryString(valorRT), '0', 32), 2);
				
				if (valorRSBig < valorRTBig) {
					resultado = 1;
				} else {
					resultado = 0;
				}
				
				state.writeRegister(rd, resultado);
			break;
			
		case "000000":	//FUN플O SLL
				valorRT = Integer.parseInt(BinarioComSinal(Integer.toBinaryString(valorRT), 32), 2);
				
				if (shamt != 0) {
					resultado = valorRT << shamt;
					//for (int i = 1; i < shamt; i++) {
					//	resultado *= 2;
					//}
				} else {
					resultado = valorRT;
				}
				
				state.writeRegister(rd, resultado);
			break;
			
		case "000010": // FUN픈O SRL
				valorRT = Integer.parseInt(BinarioComSinal(Integer.toBinaryString(valorRT), 32), 2);
				if(shamt != 0) {
					resultado = valorRT >> shamt;
				}else {
					resultado = valorRT;
				}
				state.writeRegister(rd, resultado);
			break;
			
		case "100010": //FUN플O SUB
				valorRS = Integer.parseInt((BinarioComSinal(Integer.toBinaryString(valorRS), 32)), 2);
				valorRT = Integer.parseInt(BinarioComSinal(Integer.toBinaryString(valorRT), 32), 2);
				resultado = valorRS - valorRT;
				state.writeRegister(rd, resultado);
			break;
			
		case "100011": //FUN플O SUBU
				valorRS = Integer.parseInt((BinarioComSinal(Integer.toBinaryString(valorRS), 32)), 2);
				valorRT = Integer.parseInt(BinarioComSinal(Integer.toBinaryString(valorRT), 32), 2);
				resultado = valorRS - valorRT;
				state.writeRegister(rd, resultado);
			break;
			
		default:
			break;
		}
		
	}
	
	public static void InstTipoI (String instrucaoAtual, State state) {
		Integer rs = Integer.parseInt((instrucaoAtual.substring(6, 11)), 2);
		Integer rt = Integer.parseInt((instrucaoAtual.substring(11, 16)), 2);
		Integer constantOrAddress = Integer.parseInt(instrucaoAtual.substring(16, 32), 2);
		Integer valorRS = state.readRegister(rs);
		Integer valorRT = state.readRegister(rt);
		Integer result = 0;
		String subStringRT = "";
		String StringRT = "";
		Integer dadoMemoria = 0;
		String opcode = instrucaoAtual.substring(0, 6);
		String texto = "";
		
		switch(opcode) {
			case "001000": //FUN플O ADDI CONFIRAM SE FIZ CERTO. ***
				texto = instrucaoAtual.substring(16, 32);
				if((texto.charAt(0)) == '1') {
					constantOrAddress = Integer.parseInt(BinarioComSinal(completeToLeft(instrucaoAtual.substring(16, 32), '1', 32), 32), 2);
				}else {
					constantOrAddress = Integer.parseInt(BinarioComSinal(instrucaoAtual.substring(16, 32), 32), 2);
				}
				
				result = valorRS + constantOrAddress;
				state.writeRegister(rt, result);
			break;
			
			case "001001": //FUN픈O ADDIU
				texto = instrucaoAtual.substring(16, 32);
				if((texto.charAt(0)) == '1') {
					constantOrAddress = Integer.parseInt(BinarioComSinal(completeToLeft(instrucaoAtual.substring(16, 32), '1', 32), 32), 2);
				}else {
					constantOrAddress = Integer.parseInt(BinarioComSinal(instrucaoAtual.substring(16, 32), 32), 2);
				}				
				
				result = valorRS + constantOrAddress;
				state.writeRegister(rt, result);
			break;
			
			case "001100": //FUN플O ANDI								
				texto = instrucaoAtual.substring(16, 32);
				if((texto.charAt(0)) == '1') {
					constantOrAddress = Integer.parseInt(BinarioComSinal(completeToLeft(instrucaoAtual.substring(16, 32), '1', 32), 32), 2);
				}else {
					constantOrAddress = Integer.parseInt(BinarioComSinal(instrucaoAtual.substring(16, 32), 32), 2);
				}
				
				result = valorRS & constantOrAddress;
				state.writeRegister(rt, result);
			break;
				
			case "001101": // FUN플O ORI
				texto = instrucaoAtual.substring(16, 32);
				if((texto.charAt(0)) == '1') {
					constantOrAddress = Integer.parseInt(BinarioComSinal(completeToLeft(instrucaoAtual.substring(16, 32), '1', 32), 32), 2);
				}else {
					constantOrAddress = Integer.parseInt(BinarioComSinal(instrucaoAtual.substring(16, 32), 32), 2);
				}
				
				result = valorRS | constantOrAddress;
				state.writeRegister(rt, result);
			break;
				
			case "000100": //FUN플O BEQ
				if(valorRT == valorRS) {
					char aux = instrucaoAtual.substring(16, 32).charAt(0);
					String bitZeros = "00";
					String auxConstant = "";
					String fim = "";
					short cont = 14;
					
					while(cont > 0) {
						auxConstant += aux;
						cont--;
					}
					
					String constantOrAddressText = instrucaoAtual.substring(16, 32);
					
					fim = auxConstant + constantOrAddressText + bitZeros;
					
					constantOrAddress = Integer.parseInt(BinarioComSinal(fim, 32), 2);
					result = state.getPC() + constantOrAddress + 4;
					
					state.setPC(result);
					PC4 = false;
				}
				
			break;
			
			case "000101": //FUN플O BNE
				if(valorRT != valorRS) {
					char aux = instrucaoAtual.substring(16, 32).charAt(0);
					String bitZeros = "00";
					String auxConstant = "";
					String fim = "";
					short cont = 14;
					
					while(cont > 0) {
						auxConstant += aux;
						cont--;
					}
					
					String constantOrAddressText = instrucaoAtual.substring(16, 32);
					
					fim = auxConstant + constantOrAddressText + bitZeros;
					
					constantOrAddress = Integer.parseInt(BinarioComSinal(fim, 32), 2);
					result = state.getPC() + constantOrAddress + 4;
					
					state.setPC(result);
					PC4 = false;
				}
			break;
			
			case "001010": //FUN플O STLI
				result = 0;
				
				texto = instrucaoAtual.substring(16, 32);
				if((texto.charAt(0)) == '1') {
					constantOrAddress = Integer.parseInt(BinarioComSinal(completeToLeft(instrucaoAtual.substring(16, 32), '1', 32), 32), 2);
				}else {
					constantOrAddress = Integer.parseInt(BinarioComSinal(instrucaoAtual.substring(16, 32), 32), 2);
				}	
				
				if(valorRS < constantOrAddress) {
					result = 1;
				}
				
				state.writeRegister(rt, result);
			break;
			
			case "001011": //FUN플O SLTIU
				result = 0;
				
				texto = instrucaoAtual.substring(16, 32);
				long constantOrAddressBig = 0;
				if((texto.charAt(0)) == '1') {
					constantOrAddressBig = Long.parseLong(completeToLeft(instrucaoAtual.substring(16, 32), '1', 32), 2);
				}else {
					constantOrAddressBig = Long.parseLong(completeToLeft(instrucaoAtual.substring(16, 32), '0', 32), 2);
				}

				long valorRSBig = Long.parseLong(completeToLeft(Integer.toBinaryString(valorRS), '0', 32), 2);
				
				if(valorRSBig < constantOrAddressBig) {
					result = 1;
				}
				
				state.writeRegister(rt, result);
			break;

			case "100100": // FUN플O LBU 					
					texto = instrucaoAtual.substring(16, 32);
					if((texto.charAt(0)) == '1') {
						constantOrAddress = Integer.parseInt(BinarioComSinal(completeToLeft(instrucaoAtual.substring(16, 32), '1', 32), 32), 2);
					}else {
						constantOrAddress = Integer.parseInt(BinarioComSinal(instrucaoAtual.substring(16, 32), 32), 2);
					}	
					
					dadoMemoria = state.readWordDataMemory(valorRS + constantOrAddress);
					
					if(dadoMemoria < 0) {
						texto = completeToLeft(Integer.toBinaryString(dadoMemoria), '1', 32);
					}else {
						texto = completeToLeft(Integer.toBinaryString(dadoMemoria), '0', 32);
					}	
					
					Integer byteRT = Integer.parseInt(completeToLeft((texto).substring(24, 32), '0', 32), 2);
					
					state.writeRegister(rt, byteRT);
				break;

			case "100101": // FUN플O LHU
					texto = instrucaoAtual.substring(16, 32);
					if((texto.charAt(0)) == '1') {
						constantOrAddress = Integer.parseInt(BinarioComSinal(completeToLeft(instrucaoAtual.substring(16, 32), '1', 32), 32), 2);
					}else {
						constantOrAddress = Integer.parseInt(BinarioComSinal(instrucaoAtual.substring(16, 32), 32), 2);
					}	
					
					dadoMemoria = state.readWordDataMemory(valorRS + constantOrAddress);
					
					if(dadoMemoria < 0) {
						texto = completeToLeft(Integer.toBinaryString(dadoMemoria), '1', 32);
					}else {
						texto = completeToLeft(Integer.toBinaryString(dadoMemoria), '0', 32);
					}	
					
					Integer halfRT = Integer.parseInt(completeToLeft((texto).substring(16, 32), '0', 32), 2);
	
					state.writeRegister(rt, halfRT);
				break;
				
			case "001111": //FUN플O LUI 
					String adress = instrucaoAtual.substring(16, 32);
					valorRT = Integer.parseInt(BinarioComSinal((adress + "0000000000000000"), 32), 2); // 16 0's
					
					state.writeRegister(rt, valorRT);
				break;
				
			case "100011": //FUN플O LW
				texto = instrucaoAtual.substring(16, 32);
				if((texto.charAt(0)) == '1') {
					constantOrAddress = Integer.parseInt(BinarioComSinal(completeToLeft(instrucaoAtual.substring(16, 32), '1', 32), 32), 2);
				}else {
					constantOrAddress = Integer.parseInt(BinarioComSinal(instrucaoAtual.substring(16, 32), 32), 2);
				}
				
				dadoMemoria = state.readWordDataMemory(valorRS + constantOrAddress);

				state.writeRegister(rt, dadoMemoria);
				
				break;
				
			case "101000": //FUN플O SB
				texto = instrucaoAtual.substring(16, 32);
				if((texto.charAt(0)) == '1') {
					constantOrAddress = Integer.parseInt(BinarioComSinal(completeToLeft(instrucaoAtual.substring(16, 32), '1', 32), 32), 2);
				}else {
					constantOrAddress = Integer.parseInt(BinarioComSinal(instrucaoAtual.substring(16, 32), 32), 2);
				}	
		
				subStringRT = completeToLeft(Integer.toBinaryString(state.readRegister(rt)), '0', 32).substring(24, 32);

				Integer dadoByteMemoria = state.readWordDataMemory(valorRS + constantOrAddress);
				
				if(dadoByteMemoria < 0) {
					texto = completeToLeft(Integer.toBinaryString(dadoByteMemoria), '1', 32);
				}else {
					texto = completeToLeft(Integer.toBinaryString(dadoByteMemoria), '0', 32);
				}	
				
				StringRT = (texto).substring(0, 24) + subStringRT;
				
				valorRT = Integer.parseInt(BinarioComSinal(StringRT, 32), 2);
				
				state.writeWordDataMemory((valorRS + constantOrAddress), valorRT);
				
				break;
				
			case "101001": //FUN플O SH
				texto = instrucaoAtual.substring(16, 32);
				if((texto.charAt(0)) == '1') {
					constantOrAddress = Integer.parseInt(BinarioComSinal(completeToLeft(instrucaoAtual.substring(16, 32), '1', 32), 32), 2);
				}else {
					constantOrAddress = Integer.parseInt(BinarioComSinal(instrucaoAtual.substring(16, 32), 32), 2);
				}	
		
				subStringRT = completeToLeft(Integer.toBinaryString(state.readRegister(rt)), '0', 32).substring(16, 32);

				Integer dadoHalfMemoria = state.readWordDataMemory(valorRS + constantOrAddress);
				
				if(dadoHalfMemoria < 0) {
					texto = completeToLeft(Integer.toBinaryString(dadoHalfMemoria), '1', 32);
				}else {
					texto = completeToLeft(Integer.toBinaryString(dadoHalfMemoria), '0', 32);
				}	
				
				StringRT = (texto).substring(0, 16) + subStringRT;
				
				valorRT = Integer.parseInt(BinarioComSinal(StringRT, 32), 2);
				
				state.writeWordDataMemory((valorRS + constantOrAddress), valorRT);
				
				break;
				
			case "101011": //FUN플O SW
				texto = instrucaoAtual.substring(16, 32);
				if((texto.charAt(0)) == '1') {
					constantOrAddress = Integer.parseInt(BinarioComSinal(completeToLeft(instrucaoAtual.substring(16, 32), '1', 32), 32), 2);
				}else {
					constantOrAddress = Integer.parseInt(BinarioComSinal(instrucaoAtual.substring(16, 32), 32), 2);
				}	

				state.writeWordDataMemory((valorRS + constantOrAddress), state.readRegister(rt));
				break;	
			
			default:
				break;
			}
	}
	
	public static void InstTipoJ (String instrucaoAtual, State state) {
		String opcode = instrucaoAtual.substring(0, 6);
		String adress = instrucaoAtual.substring(6, 32);
		Integer jumpAddress = 0;
		String PCcompleto = "";
		
		switch (opcode) {
		case "000010": // FUN플O J
				PCcompleto = completeToLeft((Integer.toString(state.getPC() + 4)), '0', 32);
				jumpAddress = Integer.parseInt(BinarioComSinal((PCcompleto.substring(0, 4) + adress + "00"), 32), 2);
				state.setPC(jumpAddress);
				PC4 = false;
			break;
				
		case "000011": // FUN플O JAL
				state.writeRegister(31, (state.getPC() + 4));
				PCcompleto = completeToLeft((Integer.toString(state.getPC() + 4)), '0', 32);
				jumpAddress = Integer.parseInt(BinarioComSinal((PCcompleto.substring(0, 4) + adress + "00"), 32), 2);
				state.setPC(jumpAddress);
				PC4 = false;			
			break;
			
		default:
			break;
		}
	}
}