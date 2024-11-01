export enum FieldType {
    Input = "input",
    Select = "select",
    Checkbox = "checkbox",
    Date = "date",
    DateRange = "date_range"
}

export enum ComponentType {
    Models = 'models',
    Output = 'outConnectors',
    Input = 'inConnectors',
    Rules = 'rules',
    OperationRules = 'OperationRules',
    VariableRules = 'VariableRules',
    FormRules = 'FormRules',
}

export enum ActionsType {
    ADDED = "add",
    SET = "set",
    REMOVE = "remove",
    SAVE = "save"
}