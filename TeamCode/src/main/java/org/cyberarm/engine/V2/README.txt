CyberarmEngine V2 Architecture

Engine
  -> [States]
    -> [ParallelStates]

Start with an Engine and override setup():

public class Engine extends CyberarmEngineV2 {
    public void setup() {
        addState(new State(arguments));
    }
}

NOTE: states do not need to be passed the instance of Engine as they have a field 'cyberarmEngine'
      which is set to CyberarmEngineV2.instance when they are created.

States can have 'children' which are also States, which run in parallel with their parent. There is
no fixed limit to how many grandchildren can exist (Children themselves can have children.):

public class State extends CyberarmEngineStateV2 {
    public init() {
      addParallelState(new ParallelState(arguments));
    }

    public exec() {
    }
}