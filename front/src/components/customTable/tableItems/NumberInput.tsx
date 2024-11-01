import { InputNumber } from 'antd';
import { useDispatch } from 'react-redux';
import { ActionTypes as InputConActionTypes } from "../../../redux/Model/ConnectorInputReducer";
import { ActionTypes as OutputConActionTypes } from "../../../redux/Model/ConnectorOuputReducer";

function NumberInput({ defaultValue, id, fieldId, type }:
	{ defaultValue: number; id: string | number; fieldId: 'maxSize' | 'maxArray', type?: string }) {
	const dispatch = useDispatch();

	const onClick = (e: any) => {
		e.stopPropagation();
		dispatch({
			type: type === 'input' ? InputConActionTypes.CONNECTOR_PICK_ROW : OutputConActionTypes.CONNECTOR_PICK_ROW,
			payload: {
				field: id,
			},
		});
	};

	const setNewField = (newValue: number | null) => {
		setTimeout(() => {
			dispatch({
				type: type === 'input' ? InputConActionTypes.CONNECTOR_UPDATE_ROW : OutputConActionTypes.CONNECTOR_UPDATE_ROW,
				payload: {
					key: fieldId === "maxSize" ? "maxSize" : "maxArray",
					data: newValue,
				},
			});
		}, 1000);
	};

	return (
		<span onClick={(e) => onClick(e)}>
			{defaultValue ? <InputNumber
				style={{ width: "90%" }}
				min={0}
				max={99999}
				defaultValue={defaultValue}
				onClick={(e) => onClick(e)}
				onChange={setNewField}
			/> :
				<InputNumber
					style={{ width: "90%" }}
					min={0}
					max={99999}
					placeholder='unknown'
					onClick={(e) => onClick(e)}
					onChange={setNewField}
				/>
			}
		</span>
	);
}

export default NumberInput;
