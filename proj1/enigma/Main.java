package enigma;

import java.io.File;
import java.io.IOException;
import java.io.PrintStream;

import java.util.HashMap;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Scanner;

import ucb.util.CommandArgs;

import static enigma.EnigmaException.*;

/** Enigma simulator.
 *  @author Darren Wang
 */
public final class Main {

    /** Process a sequence of encryptions and decryptions, as
     *  specified by ARGS, where 1 <= ARGS.length <= 3.
     *  ARGS[0] is the name of a configuration file.
     *  ARGS[1] is optional; when present, it names an input file
     *  containing messages.  Otherwise, input comes from the standard
     *  input.  ARGS[2] is optional; when present, it names an output
     *  file for processed messages.  Otherwise, output goes to the
     *  standard output. Exits normally if there are no errors in the input;
     *  otherwise with code 1. */
    public static void main(String... args) {
        try {
            CommandArgs options =
                new CommandArgs("--verbose --=(.*){1,3}", args);
            if (!options.ok()) {
                throw error("Usage: java enigma.Main [--verbose] "
                            + "[INPUT [OUTPUT]]");
            }

            _verbose = options.contains("--verbose");
            new Main(options.get("--")).process();
            return;
        } catch (EnigmaException excp) {
            System.err.printf("Error: %s%n", excp.getMessage());
        }
        System.exit(1);
    }

    /** Open the necessary files for non-option arguments ARGS (see comment
      *  on main). */
    Main(List<String> args) {
        _config = getInput(args.get(0));

        if (args.size() > 1) {
            _input = getInput(args.get(1));
        } else {
            _input = new Scanner(System.in);
        }

        if (args.size() > 2) {
            _output = getOutput(args.get(2));
        } else {
            _output = System.out;
        }
    }

    /** Return a Scanner reading from the file named NAME. */
    private Scanner getInput(String name) {
        try {
            return new Scanner(new File(name));
        } catch (IOException excp) {
            throw error("could not open %s", name);
        }
    }

    /** Return a PrintStream writing to the file named NAME. */
    private PrintStream getOutput(String name) {
        try {
            return new PrintStream(new File(name));
        } catch (IOException excp) {
            throw error("could not open %s", name);
        }
    }

    /** Configure an Enigma machine from the contents of configuration
     *  file _config and apply it to the messages in _input, sending the
     *  results to _output. */
    private void process() {
        Machine machine = readConfig();
        while (_input.hasNext("(?<=^|\\n)\\\\*.*")) {
            String[] rotors = new String[machine.numRotors()];
            _input.next(); // for skipping the starting asterisk
            for (int i = 0; i < machine.numRotors(); i++) {
                rotors[i] = _input.next();
            }
            machine.insertRotors(rotors);
            setUp(machine, _input.next());
            String cycle = "";
            while (_input.hasNext(".*[\\(|\\)]+.*")) {
                cycle += _input.next();
            }
            machine.setPlugboard(new Permutation(cycle, _alphabet));
            while (_input.hasNextLine() && !_input.hasNext("(?<=^|\n)\\*.*")) {
                String nextLine = _input.nextLine().replaceAll("\\s", "");
                printMessageLine(machine.convert(nextLine));
            }
        }
    }

    /** Return an Enigma machine configured from the contents of configuration
     *  file _config. */
    private Machine readConfig() {
        try {
            _alphabet = new Alphabet(_config.next());
            int nRotors = _config.nextInt();
            int nPawls = _config.nextInt();
            HashMap<String, Rotor> rotors = new HashMap<>();
            while (_config.hasNext()) {
                Rotor rotor = readRotor();
                rotors.put(rotor.name(), rotor);
            }
            return new Machine(_alphabet, nRotors, nPawls, rotors.values());
        } catch (NoSuchElementException excp) {
            throw error("configuration file truncated");
        }
    }

    /** Return a rotor, reading its description from _config. */
    private Rotor readRotor() {
        try {
            Rotor rotor;
            String name = _config.next();
            String typeNotch = _config.next();
            Character type = typeNotch.charAt(0);
            String notch = typeNotch.substring(1);
            String cycle = "";
            while (_config.hasNext(".*[\\\\(|\\\\)]+.*")) {
                cycle += _config.next();
            }
            Permutation permutation = new Permutation(cycle, _alphabet);
            if (type == 'M') {
                rotor = new MovingRotor(name, permutation, notch);
            } else if (type == 'N') {
                rotor = new FixedRotor(name, permutation);
            } else if (type == 'R') {
                rotor = new Reflector(name, permutation);
            } else {
                throw new EnigmaException("No such type of rotor");
            }
            return rotor;
        } catch (NoSuchElementException excp) {
            throw error("bad rotor description");
        }
    }

    /** Set M according to the specification given on SETTINGS,
     *  which must have the format specified in the assignment. */
    private void setUp(Machine M, String settings) {
        M.setRotors(settings);
    }

    /** Return true iff verbose option specified. */
    static boolean verbose() {
        return _verbose;
    }

    /** Print MSG in groups of five (except that the last group may
     *  have fewer letters). */
    private void printMessageLine(String msg) {
        for (int i = 0; i < msg.length(); i++) {
            _output.print(msg.charAt(i));
            if (((i + 1) % 5 == 0) && ((i + 1) != msg.length())) {
                _output.print(" ");
            }
        }
        _output.print("\n");
    }

    /** Alphabet used in this machine. */
    private Alphabet _alphabet;

    /** Source of input messages. */
    private Scanner _input;

    /** Source of machine configuration. */
    private Scanner _config;

    /** File for encoded/decoded messages. */
    private PrintStream _output;

    /** True if --verbose specified. */
    private static boolean _verbose;
}