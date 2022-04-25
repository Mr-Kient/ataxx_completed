package gitlet;

import java.util.Objects;
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
            throw error("No command detected.");
        }

        String cases = args[0];

        if (cases.equals("init")) {
            validateArg(args, 1);
            if (CWD.exists()) {
                throw error("Gitlet CWD already exists.");
            }
            init();
        }

        if (cases.equals("add")) {
            validateArg(args, 2);
            if (!join(args[1]).exists()) {
                throw error("File does not exist.");
            }
            add(args[1]);
        }

        if (cases.equals("commit")) {
            if (args.length != 2) {
                throw error("Please enter a commit message.");
            }
            commit(args[1]);
        }

        if (cases.equals("rm")) {
            validateArg(args, 2);
            rm(args[1]);
        }

        if (cases.equals("log")) {
            validateArg(args, 1);
            log();
        }

        if (cases.equals("global-log")) {
            validateArg(args, 1);
            globalLog();
        }

        if (cases.equals("find")) {
            validateArg(args, 2);
            find(args[1]);
        }

        if (cases.equals("status")) {
            validateArg(args, 1);
            status();
        }

        if (cases.equals("checkout")) {
            if (args.length == 4) {
                checkoutPastFile(args[1], args[3]);
            } else if (args.length == 3) {
                checkoutHeadFile(args[2]);
            } else if (args.length == 2) {
                checkoutBranch(args[1]);
            } else {
                throw error("Not a valid input for ",
                        "checkout");
            }
        }

        if (cases.equals("branch")) {
            validateArg(args, 2);
            branch(args[1]);
        }

        if (cases.equals("rm-branch")) {
            validateArg(args, 2);
            rmBranch(args[1]);
        }

        if (cases.equals("branch")) {
            validateArg(args, 2);
            reset(args[1]);
        }

        if (cases.equals("merge")) {
            validateArg(args, 2);
            merge(args[1]);
        }

    }

    public static void validateArg(String[] args, int req) {
        if (args.length != req) {
            throw error("Invalid number of arguments.");
        }
    }

}
