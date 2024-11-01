import { Box, Flex } from "@chakra-ui/react";
import React, { FC, useEffect, useRef, useState } from "react";
import { Loader } from "../../../../components/Loader/Loader";
import { useAppSelector } from "../../../../redux/Store";
import { Grid as GridDHX } from "dhx-suite";
import { BoxStyleConfig } from "../../../Forms/FormStyleConfigs";
import ToolbarGridModel from "../../../../components/DHX/ToolbarGridModel";
import { ActionsType, ComponentType } from "../../../../common/constants";
import { useFormikContext } from "formik";
import { DHTMLXTypes } from "../../../../redux/DHTMLX/DHTMLReducer";
import { useDispatch } from "react-redux";
import { RuleType } from "../../../../shared/entities/Rule/Rule";
import RulesQualityGrid from "./QualityTable";

export const GridRuleOperation: FC = () => {
	const dispatch = useDispatch();
	const formik = useFormikContext();
	const [isLoading] = useState(false);

	const [grid, setGrid] = useState<GridDHX>();
	const [idGrid, setIdGrid] = useState<string | null>(null);
	const [selectRowId, setSelectRowId] = useState<any>(null);
	const gridConnectorsRef = useRef<HTMLDivElement | null>(null);
	const event = useAppSelector(store => store.DHTMLXReducer.event)
	const rule = useAppSelector(store => store.RuleReducer.rule)


	useEffect(() => {
		if (rule && rule.jsonData) {
			// grid?.data.parse(JSON.parse(rule?.jsonData));
			// formik.setFieldValue("fields_operation", JSON.parse(rule?.jsonData))

			formik.setFieldValue("fields_operation", rule ? rule.jsonData[0] : {})
		}
	}, [rule]);

	useEffect(() => {
		if (grid) {
			grid?.events.on("change", (row: any, col: any) => {
				formik.setFieldValue("fields_operation", grid?.data.map(item => {
					const { id, ...rest } = item;
					return rest;
				}))
			});
		}
		const fieldsOperation = formik.getFieldProps("fields_operation").value;
		if (selectRowId && grid && fieldsOperation) {
			grid?.data.serialize().map((v, index) => {
				if (fieldsOperation[index].condition !== v.condition) {
					if (v.id !== undefined) {
						grid.data.update(v.id, { ...v, condition: fieldsOperation[index].condition });
					}
				}
				if (selectRowId == v.id) {
					dispatch({
						type: DHTMLXTypes.EVENT_TOOLTIP,
						payload: { actions: ComponentType.FormRules, id: index, item: v.condition }
					});
					formik.setFieldValue("condition", v.condition)
					formik.setFieldValue("selCondition", index)
				}
			})
		}
	}, [selectRowId, grid]);

	useEffect(() => {
		if (event !== null && event.id === idGrid && event.componentType === ComponentType.OperationRules) {
			switch (event.actions) {
				case ActionsType.ADDED:
					let last = Number(grid?.data.serialize().length) + 1;
					grid?.data.add({
						number: last,
						textValue: "HIGH",
						toIncidents: true,
						con_out: `con${last}`,
						condition: `A${last}+B${last}`
					})
					formik.setFieldValue("fields_operation", grid?.data.map(item => {
						const { id, ...rest } = item;
						return rest;
					}))
					break;
				case ActionsType.REMOVE:
					if (selectRowId !== null) {
						grid?.data.remove(selectRowId);
					}
					formik.setFieldValue("fields_operation", grid?.data.map(item => {
						const { id, ...rest } = item;
						return rest;
					}))
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
					{ minWidth: 150, id: "number", header: [{ text: "Число" }] },
					{ minWidth: 100, id: "textValue", header: [{ text: "Значение" }] },
					{ minWidth: 100, id: "toIncidents", header: [{ text: "В инциденты" }], type: "boolean" },
					{ minWidth: 100, id: "con_out", header: [{ text: "Коннектор отправки" }], type: "select" }
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
		<Flex flexDir="column" width={"70%"}>
			{isLoading ? (
				<Loader />
			) : (
				<RulesQualityGrid rule={rule} />

				// <Box {...BoxStyleConfig}>
				// 	<ToolbarGridModel idEvent={idGrid} item={ComponentType.OperationRules}
				// 		toolbarItemsVisibility={["add", "remove"]} />

				// 	<Box style={{ minHeight: "200px", width: "100%", }} ref={gridConnectorsRef} />
				// </Box>
			)}
		</Flex>
	);
};


