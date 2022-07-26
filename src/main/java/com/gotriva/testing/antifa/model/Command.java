package com.gotriva.testing.antifa.model;

/** This class represents an interpreted command. The command consists in 4 components:
 * <ol>
 *  <li>A mandatory <b>command</b>, the command name to be performed.</li>
 *  <li>A mandatory <b>object</b>, the object that should perform the command.</li>
 *  <li>An <em>optional</em> <b>parameter</b>, some actions can have a parameter like a text input.</li>
 *  <li>An <em>optional</em> <b>type</b>, the specific type of the object that should perform the action.</li>
 * </ol>
 */
public class Command {

    /** The {@link Command} builder class. */
    public static class Builder {

        /** The name of the command to be built. */
        private String command;
        /** The parameter of the command to be built. */
        private String parameter;
        /** The object of the command to be built. */
        private String object;
        /** The type of the command to be built. */
        private String type;

        private Builder() {}

        public static Builder newBuilder() {
            return new Builder();
        }

        public Builder setCommand(String command) {
            this.command = command;
            return this;
        }

        public Builder setParameter(String parameter) {
            this.parameter = parameter;
            return this;
        }

        public Builder setObject(String object) {
            this.object = object;
            return this;
        }

        public Builder setType(String type) {
            this.type = type;
            return this;
        }

        public Command build() {
            /** Assert that name and object are not null */
            assert command != null && object != null 
                : "'command' and 'object' must be not null.";
            return new Command(command, parameter, object, type);
        }      
    }

    /** The name of the command. */
    private String command;

    /** The parameter of the command. */
    private String parameter;

    /** The object of the command. */
    private String object;

    /** The object type.  */
    private String type;

    /** Default all args constructor for an command. */
    public Command(String command, String parameter, String object, String type) {
        this.command = command;
        this.parameter = parameter;
        this.object = object;
        this.type = type;
    }
    
    /** Getters and Setters */
    
    public String getCommand() {
        return command;
    }

    public String getParameter() {
        return parameter;
    }

    public void setParameter(String parameter) {
        this.parameter = parameter;
    }
    
    public boolean hasParameter() {
        return parameter != null;
    }

    public String getObject() {
        return object;
    }
    
    public String getType() {
        return type;
    }
    
    /** Override methods */

    @Override
    public String toString() {
        return "Command [command=" + command + ", object=" + object 
                + ", parameter=" + parameter + ", type=" + type + "]";
    }

    public static enum ComponentType {
        /** Represents the command name to call on the semantic structure. */
        COMMAND,
        /** Represents the command call parameter on the semantic structure. */
        PARAMETER,
        /** Represents the target object of command call on the semantic structure. */
        OBJECT,
        /** Represents the type of the target object of command call on this semantic structure. */
        TYPE,
        /** Represents a node that is a stepway to another. */
        BYPASS,
        /** Represents a node that has no function other than disambiguate the tree. */
        NO_OP
    }
}
