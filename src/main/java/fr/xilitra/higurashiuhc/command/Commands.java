package fr.xilitra.higurashiuhc.command;

import fr.xilitra.higurashiuhc.command.executor.*;

public enum Commands {
    RESSUCITE("r", new RessuciteCmd()),
    RIKATP("rika", new TeleportRikaCmd()),
    VOTE("vote", new VoteCmd()),
    BAN("ban", new BanCmd()),
    INVERSER("inverser", new InverserCmd()),
    PENSE("pense", new PenseCmd()),
    DIMENSION("dimension", new DimensionCmd()),
    FORCE("force", new ForceCmd()),
    HEAL("heal", new HealCmd()),
    LIST("list", new ListCmd()),
    ASSASSINER("assassiner", new AssassinCmd()),
    PVCMD("pv", new PvCmd()),
    SUSPECTER("suspecter", new SuspecterCmd()),
    COMPARER("comp", new ComparerCmd()),
    RIKA("rika", new RikaCmd()),
    COUPABLE("c", new CoupableCmd());

    String initials;
    CommandsExecutor commandsExecutor;

    public static Commands getCommands(String initials){
        for(Commands commands : values())
            if(initials.equals(commands.initials))
                return commands;
        return null;
    }

    Commands(String initials, CommandsExecutor ce){
        this.initials = initials;
        this.commandsExecutor = ce;
    }

    public CommandsExecutor getCommandExecutor(){
        return commandsExecutor;
    }

}
