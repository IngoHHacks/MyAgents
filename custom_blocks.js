const MOVEMENT_COLOR = 10;
const VARIABLE_COLOR = 70;
const OTHER_COLOR = 130;

const AGENTS_IMPORT = "import net.ingoh.dsl.agents.Agent"

// Creates custom blocks for the Blockly interface

// Grab item

Blockly.Blocks['Goto'] = {
    init: function() {
        this.jsonInit({
            "type": "Goto",
            "message0": "%{BKY_GOTO_TITLE}",
            // "args0": [
            //     {
            //         "type": "input_value",
            //         "name": "VALUE",
            //         "check": ["Number", "Position"]
            //     }
            // ],
            "colour": MOVEMENT_COLOR,
            "previousStatement": "",
            "nextStatement": "",
            "tooltip": "%{BKY_GOTO_TOOLTIP}"
        });
    }
}

Blockly.Java['Goto'] = function(block) {
    var value_object = Blockly.Java.valueToCode(block, "VALUE", Blockly.Java.ORDER_ATOMIC);
    var code = 'Agent.goto(' + value_object + ')\n';
    Blockly.Java.definitions_['import_agents'] = AGENTS_IMPORT;
    return code;
}