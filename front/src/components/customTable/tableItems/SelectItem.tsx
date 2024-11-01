

import { Select } from 'antd'
import { useDispatch } from 'react-redux'
import { ActionTypes as InputConActionTypes } from "../../../redux/Model/ConnectorInputReducer";
import { ActionTypes as OutputConActionTypes } from "../../../redux/Model/ConnectorOuputReducer";
import { useEffect } from 'react';
import { useAppSelector } from '../../../redux/Store';

interface DataType {
	id: number;
	name: string;
	fieldType: string;
	maxSize: number;
	allowEmpty: boolean;
	prohibitSpecCharacters: boolean;
	allowArray: boolean;
	maxArray: number;
	children: DataType[] | null;

	newItem?: boolean
}

const SelectItem = ({ defaultValue, id, type, children }: { defaultValue: string, id: string | number, type?: string, children: boolean }) => {

	const dispatch = useDispatch()
	const onClick = (e: any) => {
		e.stopPropagation()
		dispatch({
			type: type === 'input' ? InputConActionTypes.CONNECTOR_PICK_ROW : OutputConActionTypes.CONNECTOR_PICK_ROW,
			payload: {
				field: id
			}
		})
	}

	const handleChange = (value: string) => {
		dispatch({
			type: type === 'input' ? InputConActionTypes.CONNECTOR_UPDATE_ROW : OutputConActionTypes.CONNECTOR_UPDATE_ROW,
			payload: {
				key: "fieldType",
				data: value,
			},
		});
	};

	return (
		<Select
			value={defaultValue}
			style={{ width: "90%" }}
			onClick={(e) => onClick(e)}
			onChange={handleChange}
			options={[
				{ value: "STRING", label: "STRING" },
				{ value: "INTEGER", label: "INTEGER" },
				{ value: "LONG", label: "LONG" },
				{ value: "DOUBLE", label: "DOUBLE" },
				{ value: "OBJECT", label: "OBJECT" },
			]}
		/>
	)
}

export default SelectItem