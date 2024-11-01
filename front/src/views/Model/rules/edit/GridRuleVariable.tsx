import {Box, Flex} from "@chakra-ui/react";
import React, {FC, useEffect, useRef, useState} from "react";
import {Loader} from "../../../../components/Loader/Loader";
import {useAppSelector} from "../../../../redux/Store";
import {Grid as GridDHX} from "dhx-suite";
import {BoxStyleConfig} from "../../../Forms/FormStyleConfigs";
import ToolbarGridModel from "../../../../components/DHX/ToolbarGridModel";
import {ActionsType, ComponentType} from "../../../../common/constants";
import {useFormikContext} from "formik";

export const GridRuleVariable: FC = () => {
    const formik = useFormikContext();
    const [isLoading] = useState(false);
    const [selectRowId, setSelectRowId] = useState<any>(null);

    const [grid, setGrid] = useState<GridDHX>();
    const [idGrid, setIdGrid] = useState<string | null>(null);
    const gridConnectorsRef = useRef<HTMLDivElement | null>(null);
    const event = useAppSelector(store => store.DHTMLXReducer.event)
    const rule = useAppSelector(store => store.RuleReducer.rule)

    useEffect(() => {
        if (rule && rule.fields) {
            grid?.data.parse(rule.fields);
        }
    }, [rule]);

    useEffect(() => {
        if (grid) {
            grid?.events.on("change", (row: any, col: any) => {
                formik.setFieldValue("fields", grid?.data.serialize().map((v) => ({name: v.name})))
            });
        }
        if (event !== null && event.id === idGrid && event.componentType === ComponentType.VariableRules) {
            switch (event.actions) {
                case ActionsType.ADDED:
                    let last = Number(grid?.data.serialize().length) + 1;
                    grid?.data.add({name: `newField${last}`, link: `link${last}`})
                    formik.setFieldValue("fields", grid?.data.serialize().map((v) => ({name: v.name})))
                    break;
                case ActionsType.REMOVE:
                    if (selectRowId !== null) {
                        grid?.data.remove(selectRowId);
                    }
                    formik.setFieldValue("fields", grid?.data.serialize().map((v) => ({name: v.name})))
                    break;
                default:
                    break;
            }
        }
    }, [event, grid]);

    useEffect(() => {
        if (gridConnectorsRef.current && !grid) {
            const newGrid = new GridDHX(gridConnectorsRef.current, {
                columns: [
                    {minWidth: 150, id: "name", header: [{text: "Наименование"}]}
                ],
                editable: true,
                autoWidth: true,
                selection: "row",
                rowHeight: 30
            });
            newGrid?.events.on("BeforeSelect", (row: any, col: any) => {
                setSelectRowId(row.id)
            });
            setGrid(newGrid);
            setIdGrid("grid_" + Math.floor(Math.random() * 10000))
        }
    }, [grid])

    return (
        <Flex flexDir="column" width={"100%"}>
            {isLoading ? (
                <Loader/>
            ) : (
                <Box {...BoxStyleConfig}>
                    <ToolbarGridModel idEvent={idGrid} item={ComponentType.VariableRules}
                                      toolbarItemsVisibility={["add", "remove"]}/>
                    <Box style={{minHeight: "200px", width: "100%",}} ref={gridConnectorsRef}/>
                </Box>
            )}
        </Flex>
    );
};


