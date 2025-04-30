package net.ingoh.myagents.lang.il;

public class ParameterIL implements ILNode {
        public String name;
        public String type;

        public static ParameterILBuilder builder() {
            return new ParameterILBuilder();
        }

        public static class ParameterILBuilder {
            private final ParameterIL parameterIL;

            public ParameterILBuilder() {
                this.parameterIL = new ParameterIL();
            }

            public ParameterILBuilder name(String name) {
                this.parameterIL.name = name;
                return this;
            }

            public ParameterILBuilder type(String type) {
                this.parameterIL.type = type;
                return this;
            }

            public ParameterIL build() {
                return this.parameterIL;
            }
        }
}
