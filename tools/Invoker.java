package tools;

import command.*;
import java.util.HashMap;

/**
 * Класс отвечающий за распознавание команды.
 * @author mike
 */
public class Invoker {
    
    private final HashMap<String, Command> commandMap = new HashMap<>();
    
    String[] args;
    
    public Invoker(String ... args) {
        this.args = args;
        commandMap.put("help", new CommandHelp(args));
        commandMap.put("info", new CommandInfo(args));
        commandMap.put("show", new CommandShow(args));
        commandMap.put("add", new CommandAdd(args));
        commandMap.put("update", new CommandUpdate(args));
        commandMap.put("remove_by_id", new CommandRemove(args));
        commandMap.put("clear", new CommandClear(args));
        commandMap.put("execute_script", new CommandExecute(args));
        commandMap.put("exit", new CommandExit(args));
        commandMap.put("add_if_min", new CommandAddIfMin(args));
        commandMap.put("remove_lower", new CommandRemoveLower(args));
        commandMap.put("filter_less_than_status", new CommandFilterContains(args));
        commandMap.put("filter_contains_name", new CommandFilterStatus(args));
        commandMap.put("print_ascending", new CommandPrint(args));
    }
    
    public Command invoke() {
        return commandMap.get(args[0]);
    }
    
    public boolean contains(String command) {
        return commandMap.containsKey(command);
    }
    
}
