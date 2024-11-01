
import { Checkbox } from 'antd'
import type { CheckboxProps } from 'antd';
import { useDispatch } from 'react-redux';
import { ActionTypes as InputConActionTypes } from "../../../redux/Model/ConnectorInputReducer";
import { ActionTypes as OutputConActionTypes } from "../../../redux/Model/ConnectorOuputReducer";

function CheckBoxItem({ defaultValue, id, fieldId, type }:
	{
		defaultValue: boolean,
		id: number | string,
		type?: string,
		fieldId: 0 | 1 | 2
	}) {

	const dispatch = useDispatch()
	const onChange: CheckboxProps['onChange'] = (e) => {
		console.log(`${fieldId} = ${e.target.checked}`);

		dispatch({
			type: type === 'input' ? InputConActionTypes.CONNECTOR_PICK_ROW : OutputConActionTypes.CONNECTOR_PICK_ROW,
			payload: {
				field: id
			}
		})

		dispatch({
			type: type === 'input' ? InputConActionTypes.CONNECTOR_UPDATE_ROW : OutputConActionTypes.CONNECTOR_UPDATE_ROW,
			payload: {
				key: fieldId === 0 ? 'allowEmpty'
					: fieldId === 1 ? 'prohibitSpecCharacters'
						: 'allowSpecCharacters',
				data: e.target.checked,
			},
		});

	};


	return (
		<>
			{<Checkbox
				defaultChecked={defaultValue}
				onChange={onChange}
				onClick={(e) => { e.stopPropagation() }}
				style={{ width: '100%', height: '100%', display: 'flex', justifyContent: 'center', alignItems: 'center' }}
			/>}
		</>
	)
}

export default CheckBoxItem