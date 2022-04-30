package gitlet;

import static gitlet.Command.init;
import static gitlet.Files.*;
import static gitlet.Utils.*;
import static gitlet.Command.*;

/** Driver class for Gitlet, the tiny stupid version-control system.
 *  @author Darren Wang
 */
public class Main {

    /** Usage: java gitlet.Main ARGS, where ARGS contains
     *  <COMMAND> <OPERAND> .... */
    public static void main(String... args) {
        if (args.length == 0) {
            System.out.println("Please enter a command.");
            return;
        }
        String cases = args[0];
        switch (cases) {
        case "init" -> initMain();
        case "add" -> {
            if (validateArg(args, 2)) {
                addMain(args);
            }
        }
        case "commit" -> commitMain(args);
        case "rm" -> {
            if (validateArg(args, 2)) {
                rm(args[1]);
            }
        }
        case "log" -> log();
        case "global-log" -> globalLog();
        case "find" -> {
            if (validateArg(args, 2)) {
                find(args[1]);
            }
        }
        case "status" -> status();
        case "checkout" -> checkoutMain(args);
        case "branch" -> {
            if (validateArg(args, 2)) {
                branch(args[1]);
            }
        }
        case "rm-branch" -> {
            if (validateArg(args, 2)) {
                rmBranch(args[1]);
            }
        }
        case "reset" -> {
            if (validateArg(args, 2)) {
                reset(args[1]);
            }
        }
        case "merge" -> {
            if (validateArg(args, 2)) {
                merge(args[1]);
            }
        }
        default -> System.out.println("No command with that name exists.");
        }
    }

    static void initMain() {
        if (CWD.exists()) {
            System.out.println("A Gitlet version-control system"
                    + " already exists in the current directory.");
            return;
        }
        init();
    }

    static void addMain(String... args) {
        if (!join(args[1]).exists()) {
            System.out.println("File does not exist.");
            return;
        }
        add(args[1]);
    }

    static void commitMain(String... args) {
        if (args.length != 2) {
            System.out.println("Please enter a commit message.");
            return;
        }
        commit(args[1]);
    }

    static void checkoutMain(String... args) {
        if (args.length == 4) {
            checkoutPastFile(args[1], args[3]);
        } else if (args.length == 3) {
            checkoutHeadFile(args[2]);
        } else if (args.length == 2) {
            checkoutBranch(args[1]);
        } else {
            System.out.println("Not a valid input for checkout.");
        }
    }

    public static boolean validateArg(String[] args, int req) {
        if (args.length != req) {
            System.out.println("Incorrect operands.");
            return false;
        }
        return true;
    }

}
