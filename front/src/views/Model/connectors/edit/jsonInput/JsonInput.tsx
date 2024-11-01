import { Button, Flex, Input, Modal, notification } from 'antd';
import { useState } from 'react';
import { useDispatch } from 'react-redux';
import { ActionTypes as InputConActionTypes } from '../../../../../redux/Model/ConnectorInputReducer';
import { ActionTypes as OutputConActionTypes } from '../../../../../redux/Model/ConnectorOuputReducer';

interface NestedObject {
	id: number;
	name: string;
	children?: NestedObject[];
	value?: any;
	fieldType?: string | object | number
}
function InputFromJSON({ isInputConnector }: { isInputConnector: boolean }) {

	const dispatch = useDispatch()

	const [inputValue, setInputValue] = useState<string>('');
	const [api, contextHolder] = notification.useNotification();

	const [isModalOpen, setIsModalOpen] = useState(false);

	const showModal = () => {
		setIsModalOpen(true);
	};

	const handleOk = () => {
		setIsModalOpen(false);
		handleParse()
	};

	const handleCancel = () => {
		setIsModalOpen(false);
	};


	const handleParse = () => {
		try {
			const parsedValue = JSON.parse(inputValue);
			let idCounter = 0;

			function convertObjectToArray(obj: Record<string, any>): NestedObject[] {
				return Object.entries(obj).map(([key, value], index) => {

					const type = typeof value === 'string' ? 'STRING' :
						typeof value === 'object' ? 'OBJECT' :
							typeof value === 'number' && Number.isInteger(value)
								? 'INTEGER'
								: typeof value === 'number' && !Number.isInteger(value)
									? 'DOUBLE' : 'STRING';
					const id = idCounter++;

					if (typeof value === 'object' && value !== null) {

						return {
							id,
							name: key,
							fieldType: type,
							maxArray: 255,
							allowEmpty: true,
							prohibitSpecCharacters: true,
							allowArray: false,
							maxSize: 255,
							children: convertObjectToArray(value)
						};
					} else {
						return { id, name: key, fieldType: type, maxArray: 255, allowEmpty: true, prohibitSpecCharacters: true, allowArray: false, maxSize: 255 };
					}
				});
			}

			const fieldsData = convertObjectToArray(parsedValue)

			dispatch({
				type: isInputConnector ? InputConActionTypes.CONNECTOR_IN_SET_FIELDS_TABLE : OutputConActionTypes.CONNECTOR_OUT_SET_FIELDS_TABLE,
				payload: fieldsData
			})

			// console.log('Parsed value:', fieldsData);
			api.info({
				message: `Таблица обновлена`,
				description: `Поля успешно обновлены`,
				placement: 'bottomRight',
			});
		} catch (error) {
			console.error('Error parsing JSON:', error);
			api.info({
				description: `Error parsing JSON:`,
				message: 'error',
				placement: 'bottomRight',
			});
		}
	};

	return (


		<>

			<Button type='dashed' onClick={showModal}>Parse from Json</Button>

			{contextHolder}

			<Modal open={isModalOpen} title="Paste Json here" onOk={handleOk} onCancel={handleCancel} >
				<Flex vertical gap='middle' align='flex-end' >
					<Input.TextArea
						value={inputValue}
						onChange={(e) => setInputValue(e.target.value)}
						rows={10}
						style={{ resize: 'none' }}
					/>
				</Flex>

			</Modal>

		</>





	);
}

export default InputFromJSON;
