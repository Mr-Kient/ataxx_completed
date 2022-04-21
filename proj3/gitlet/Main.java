package gitlet;

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

        switch (args[0]) {
            case "init":
                validateArg("init", args, 1);
                if (join(".gitlet").exists()) {
                    throw error("Gitlet already exists in CWD.");
                }
                init();
                break;

            case "add":
                validateArg("add", args, 2);
                if (!join(args[1]).exists()) {
                    throw error("File does not exist.");
                }
                add(args[1]);
                break;

            case "commit":
                if (args.length != 2) {
                    throw error("Please enter a commit message.");
                }
                commit(args[1]);
                break;

            case "rm":
                validateArg("rm", args, 2);
                rm(args[1]);
                break;

            case "log":
                validateArg("log", args, 1);
                log();
                break;

            case "global-log":
                validateArg("global-log", args, 1);
                globalLog();
                break;

            case "find":
                validateArg("find", args, 2);
                find(args[1]);
                break;

            case "status":
                validateArg("status", args, 1);
                status();
                break;

            case "checkout":
                if (args.length == 4) {
                    checkoutPastFile(args[1], args[3]);
                }
                else if (args.length == 3) {
                    checkoutHeadFile(args[2]);
                }
                else if (args.length == 2) {
                    checkoutBranch(args[1]);
                }
                else {
                    throw error("Not a valid input for ",
                            "checkout");
                }
                break;

            case "branch":
                validateArg("branch", args, 2);
                branch(args[1]);
                break;

            case "rm-branch":
                validateArg("rm-branch", args, 2);
                rmBranch(args[1]);
                break;

            case "reset":
                validateArg("branch", args, 2);
                reset(args[1]);
                break;

            case "merge" :
                validateArg("merge", args, 2);
                merge(args[1]);
                break;

            default:
                throw error("Invalid command.");
        }
    }

    public static void validateArg(String cmd, String[] args, int req) {
        if (args.length != req) {
            throw error("Invalid number of arguments for ", cmd);
        }
    }

}
