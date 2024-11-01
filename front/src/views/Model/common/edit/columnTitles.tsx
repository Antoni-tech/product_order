import { InputNumber, Popover, Tooltip } from 'antd'
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome'
import {
	faAnglesRight,
	faFileWord,
	faCircleCheck,
	faFont,
	fa1,
	faImage,
	faFileCode,
	faSheetPlastic,
	faQ,
	faPlus,
	faJ,
	faPause,
	faSpinner,
	faCodePullRequest,
	faLayerGroup,
	faICursor,
	faCode, faEllipsis,
	faPlay, faSitemap, faVideo, faVolumeHigh, faQuestion
} from '@fortawesome/free-solid-svg-icons'

import { Badge } from 'antd'
import { URLPaths } from '../../../../config/application/URLPaths';

import { CaretUpOutlined } from '@ant-design/icons';
import { IModelStructComponents } from '../../../../shared/entities/Connector/Connector';
import ModelLinkButton from './ModelLinkButton';
import { useState } from 'react';
import { useDispatch } from 'react-redux';
import { ActionTypes } from '../../../../redux/Model/ModelReducer';

const shared = process.env.PUBLIC_URL + '/static/common.svg';
const defaultRule = process.env.PUBLIC_URL + '/static/defaultRule.svg';
const unstructData = process.env.PUBLIC_URL + '/static/unstruct.svg';

export const inputColumns = () => {

	return [
		{
			title: 'state',
			key: 'state',
			dataIndex: 'state',
			width: '5%',
			render: (text: string) => <FontAwesomeIcon icon={text === 'STOP' ? faPlay : text === 'PAUSE' ? faPlay : faPause} />
		},

		{
			title: 'name',
			dataIndex: 'name',
			key: 'name',
			ellipsis: true,
			width: '40%',
			render: (text: string, record: IModelStructComponents) =>
				<Tooltip title={`${text}`}>
					<ModelLinkButton link={`${URLPaths.IN_CONNECTOR_EDIT.link}`} id={record.modelComponentId} text={text} />
				</Tooltip>
		},
		{
			title: 'Тип',
			dataIndex: 'subtype',
			key: 'subtype',
			align: 'center',
			render: (text: string) =>
				<Tooltip title={text}>
					<FontAwesomeIcon icon={text === 'AWAITINGER' ? faSpinner : faCodePullRequest} />
				</Tooltip>,
			width: '5%'
		},
		{
			title: 'Общее',
			dataIndex: 'shared',
			key: 'shared',
			width: '5%',
			align: 'center',
			render: (text: string) =>
				<Tooltip title='Общее'>
					<span style={{ width: '100%', height: '22px', display: 'flex', justifyContent: 'center', alignItems: 'flex-end' }}>
						<img src={shared} alt="running" style={{ width: 20, paddingLeft: '0px' }} />
					</span>
				</Tooltip>
		},
		{
			title: 'Вид данных',
			dataIndex: 'dataType',
			key: 'dataType',
			width: '5%',
			align: 'center',
			render: (text: string) =>
				<Tooltip title={`Вид данных: ${text}`}>
					{text === 'UNSTRUCTURED_DATA' ?
						<span style={{ width: '100%', height: '22px', display: 'flex', justifyContent: 'center', alignItems: 'flex-end' }}>
							<img src={unstructData} alt="running" style={{ width: 20, paddingLeft: '0px' }} />
						</span>
						:
						<FontAwesomeIcon icon={text === 'STRUCTURED_DATA' ? faSitemap :
							text === 'VIDEO_STREAM' ? faVideo :
								text === 'VIDEO_STREAM' ? faVolumeHigh :
									faQuestion} />}
				</Tooltip>
		},
		{
			title: 'Формат',
			dataIndex: 'dataFormat',
			key: 'dataFormat',
			width: '5%',
			align: 'center',
			render: (text: string) =>
				<Tooltip title={`Формат данных: ${text}`}>
					<FontAwesomeIcon icon={
						text === 'JSON' ? faJ :
							text === 'TEXT' ? faFont :
								text === 'HTML' ? faFileCode :
									text === 'XML' ? faCode :
										faEllipsis

					} />
				</Tooltip>
		},
		{
			title: 'изменения подтверждены',
			dataIndex: 'summaryState',
			key: 'summaryState',
			width: '5%',
			align: 'center',
			render: (text: string) =>
				<Tooltip title={`${text}`}>
					<FontAwesomeIcon icon={text === 'DRAFT' ? faSheetPlastic : faCircleCheck} />
				</Tooltip>
		},
		{
			title: 'Результат',
			dataIndex: 'resultIncremental',
			key: 'resultIncremental',
			width: '5%',
			align: 'center'
		},
		{
			title: 'Кол-во операций',
			dataIndex: 'amountOfTransactions',
			key: 'amountOfTransactions',
			width: '15%',
			align: 'center',
			render: (text: string) =>
				<Tooltip title={`Кол-во операций: ${text}`}>
					<span style={{ color: 'blue' }}>{Number(text) > 99999 ? '9999+' : text}</span>
				</Tooltip>,
		},
		{
			title: 'Кол-во ошибок',
			dataIndex: 'amountOfErrors',
			key: 'amountOfErrors',
			width: '5%',
			align: 'center',
			render: (text: string) =>
				<Tooltip title={`Кол-во ошибок: ${text}`}>
					<span style={{ color: 'red' }}>{Number(text) > 999 ? '99+' : text}</span>
				</Tooltip>,
		},
		{
			title: 'Запуск 2 этапа',
			dataIndex: 'launchSecondStage',
			key: 'launchSecondStage',
			width: '5%',
			align: 'center',
			render: (text: boolean, record: any) =>
				<CustomSecondStageStatusHandler text={text} record={record} />,
		}
	]

}


export const rulesColumns = [
	{
		title: 'state',
		key: 'state',
		dataIndex: 'state',
		width: '5%',
		render: (text: string) => <FontAwesomeIcon icon={text === 'STOP' ? faPlay : text === 'PAUSE' ? faPlay : faPause} />

	},
	{
		title: 'Очередность',
		dataIndex: 'queueNumber',
		key: 'queueNumber',
		align: 'center',
		width: '5%',
		// defaultSortOrder: 'ascend',
		sortOrder: 'ascend', // Сортировка по возрастанию по умолчанию
		sorter: (a: { queueNumber: number }, b: { queueNumber: number }) => a.queueNumber - b.queueNumber,
		render: (text: string, record: any) => <CustomQueueNumber text={text} record={record} />,
	},

	{
		title: 'name',
		dataIndex: 'name',
		key: 'name',
		ellipsis: true,
		render: (text: string, record: IModelStructComponents) =>
			<Tooltip title={`${text}`}>
				<ModelLinkButton link={`${URLPaths.RULES_EDIT.link}`} id={record.modelComponentId} text={text} />
			</Tooltip>,
		width: '40%'
	},
	{
		title: 'Тип',
		dataIndex: 'type',
		key: 'type',
		render: (text: string) =>
			<Tooltip title={`DEFAULT_${text}`}>
				{/* <FontAwesomeIcon icon={text === 'RULE' ? faBrain : text === 'RULE_AI' ? faCodeBranch :
					text === 'RULE_ML' ? faHardDrive : faGraduationCap} /> */}
				<span style={{ width: '100%', height: '22px', display: 'flex', justifyContent: 'center', alignItems: 'flex-end' }}>
					<img src={defaultRule} alt="running" style={{ width: 20, paddingLeft: '0px' }} />
				</span>
			</Tooltip>
		,
		align: 'center',
		width: '5%'
	},

	{
		title: 'Общее',
		dataIndex: 'shared',
		key: 'shared',
		width: '5%',
		align: 'center',
		render: (text: string) =>
			<Tooltip title={`Общее`}>
				<span style={{ width: '100%', height: '22px', display: 'flex', justifyContent: 'center', alignItems: 'flex-end' }}>
					<img src={shared} alt="running" style={{ width: 20, paddingLeft: '0px' }} />
				</span>
			</Tooltip>
	},

	{
		title: 'Тип данных',
		dataIndex: 'subtype',
		key: 'subtype',
		width: '5%',
		align: 'center',
		render: (text: string) =>
			<Tooltip title={`Тип данных: ${text}`}>
				{
					text === 'QUANTITY' ?
						<Badge count={<CaretUpOutlined />} size='small'>
							<FontAwesomeIcon icon={faQ} />
						</Badge>
						: text === 'QUALITY' ?
							<FontAwesomeIcon icon={faQ} />
							: text === 'ARRAY' ?
								<FontAwesomeIcon icon={faLayerGroup} /> :
								<FontAwesomeIcon icon={faICursor} />
				}
			</Tooltip>
	},
	{
		title: 'Результат',
		dataIndex: 'resultIncremental',
		key: 'resultIncremental',
		align: 'center',
		render: (text: boolean) =>
			<Tooltip title={`Инкремент`}>
				{text === true ? <FontAwesomeIcon icon={faPlus} /> : null}
			</Tooltip>,
		width: '5%'
	},
	{
		title: 'изменения подтверждены',
		dataIndex: 'summaryState',
		key: 'summaryState',
		width: '5%',
		align: 'center',
		render: (text: string) =>
			<Tooltip title={`${text}`}>
				<FontAwesomeIcon icon={text === 'DRAFT' ? faSheetPlastic : faCircleCheck} />
			</Tooltip>
	},
	{
		title: 'Кол-во операций',
		dataIndex: 'amountOfTransactions',
		key: 'amountOfTransactions',
		align: 'center',
		render: (text: string) =>
			<Tooltip title={`Кол-во операций: ${text}`}>
				<span style={{ color: 'blue' }}>{Number(text) > 99999 ? '9999+' : text}</span>
			</Tooltip>,
		width: '15%'
	},
	{
		title: 'Кол-во ошибок',
		dataIndex: 'amountOfErrors',
		key: 'amountOfErrors',
		align: 'center',
		render: (text: string) =>
			<Tooltip title={`Кол-во ошибок: ${text}`}>
				<span style={{ color: 'red' }}>{Number(text) > 999 ? '99+' : text}</span>
			</Tooltip>,
		width: '10%'
	},
]


export const outputColumns = [
	{
		title: 'state',
		key: 'state',
		dataIndex: 'state',
		width: '5%',
		render: (text: string) => <FontAwesomeIcon icon={text === 'STOP' ? faPlay : text === 'PAUSE' ? faPlay : faPause} />

	},
	{
		title: 'name',
		dataIndex: 'name',
		key: 'name',
		width: '40%',
		ellipsis: true,
		render: (text: string, record: IModelStructComponents) =>
			<Tooltip title={`${text}`}>
				{/* <Link to={`${URLPaths.OUT_CONNECTOR_EDIT.link}/${record.modelComponentId}`}
				>{text}</Link> */}
				<ModelLinkButton link={`${URLPaths.OUT_CONNECTOR_EDIT.link}`} id={record.modelComponentId} text={text} />
			</Tooltip>
	},
	{
		title: 'Тип',
		dataIndex: 'subtype',
		key: 'subtype',
		align: 'center',
		width: '5%',
		render: (text: string) =>
			<Tooltip title={text} >
				<FontAwesomeIcon icon={faCodePullRequest} />
			</Tooltip>
	},
	{
		title: 'Общее',
		dataIndex: 'shared',
		key: 'shared',
		width: '5%',
		align: 'center',
		render: (text: string) =>
			<Tooltip title={`Общее`} >
				<span style={{ width: '100%', height: '22px', display: 'flex', justifyContent: 'center', alignItems: 'flex-end' }}>
					<img src={shared} alt="running" style={{ width: 20, paddingLeft: '0px' }} />
				</span>
			</Tooltip>
	},
	{
		title: 'Вид данных',
		dataIndex: 'dataType',
		key: 'dataType',
		width: '5%',
		align: 'center',
		render: (text: string) =>
			<Tooltip title={`Вид данных: ${text}`}>
				{text === 'UNSTRUCTURED_DATA' ?
					<span style={{ width: '100%', height: '22px', display: 'flex', justifyContent: 'center', alignItems: 'flex-end' }}>
						<img src={unstructData} alt="running" style={{ width: 20, paddingLeft: '0px' }} />
					</span>
					:
					<FontAwesomeIcon icon={text === 'STRUCTURED_DATA' ? faSitemap :
						text === 'VIDEO_STREAM' ? faVideo :
							text === 'VIDEO_STREAM' ? faVolumeHigh :
								faQuestion} />}
			</Tooltip>
	},
	{
		title: 'Формат',
		dataIndex: 'dataFormat',
		key: 'dataFormat',
		width: '5%',
		align: 'center',
		render: (text: string) =>
			<Tooltip title={`Формат данных: ${text}`}>
				<FontAwesomeIcon icon={
					text === 'JSON' ? faJ :
						text === 'TEXT' ? faFont :
							text === 'HTML' ? faFileCode :
								text === 'XML' ? faCode :
									faEllipsis

				} />
			</Tooltip>
	},
	{
		title: 'Результат',
		dataIndex: 'resultIncremental',
		key: 'resultIncremental',
		width: '5%',
		align: 'center',
		render: (text: boolean) =>
			<Tooltip title={`Инкремент`}>
				{text === true ? <FontAwesomeIcon icon={faPlus} /> : null}
			</Tooltip>,
	},
	{
		title: 'изменения подтверждены',
		dataIndex: 'summaryState',
		key: 'summaryState',
		width: '5%',
		align: 'center',
		render: (text: string) =>
			<Tooltip title={`${text}`}>
				<FontAwesomeIcon icon={text === 'DRAFT' ? faSheetPlastic : faCircleCheck} />
			</Tooltip>
	},
	{
		title: 'Кол-во операций',
		dataIndex: 'amountOfTransactions',
		key: 'amountOfTransactions',
		align: 'center',
		width: '15%',
		render: (text: string) =>
			<Tooltip title={`Кол-во операций ${text}`}>
				<span style={{ color: 'blue' }}>{Number(text) > 99999 ? '9999+' : text}</span>
			</Tooltip>,
	},
	{
		title: 'Кол-во ошибок',
		dataIndex: 'amountOfErrors',
		key: 'amountOfErrors',
		align: 'center',
		width: '10%',
		render: (text: string) =>
			<Tooltip title={`Кол-во ошибок ${text}`}>
				<span style={{ color: 'red' }}>{Number(text) > 999 ? '99+' : text}</span>
			</Tooltip>,
	},
]

const CustomQueueNumber = ({ text, record }: { text: string, record: any }) => {

	const [open, setOpen] = useState(false);
	const [inputValue, setInputValue] = useState(Number(text));
	const dispatch = useDispatch()

	const hide = () => {
		setOpen(false);
	};

	const onChange = (newValue: number | null) => {
		if (newValue !== null) {
			setInputValue(newValue);

			dispatch({
				type: ActionTypes.MODEL_EDIT_CHANGE_QUEUE_NUMBER,
				payload: {
					id: record.modelComponentId,
					queueNumber: newValue
				}
			});
		}
	};


	const handleOpenChange = (newOpen: boolean) => {
		setOpen(newOpen);
	};

	return (
		<Tooltip title={`Номер в очереди: ${text}`}>

			<Popover
				content={
					<div onClick={(e) => e.stopPropagation()}>

						{/* 
						<Slider
							defaultValue={inputValue}
							onChange={onChange}
							value={typeof inputValue === 'number' ? inputValue : 0}
							keyboard
						/> */}

						<InputNumber value={typeof inputValue === 'number' ? inputValue : 0} defaultValue={inputValue} onChange={onChange} />

					</div>
				}
				title="Выставить очередь"
				trigger="click"
				open={open}
				onOpenChange={handleOpenChange}
			>
				<span style={{ cursor: 'pointer' }} onClick={(e) => e.stopPropagation()}>{text}</span>
			</Popover>
			{/* <Input value={text} style={{ height: 30, width: '100%', padding: 0 }} /> */}
		</Tooltip>
	)
}

const CustomSecondStageStatusHandler = ({ text, record }: { text: boolean, record: any }) => {

	const dispatch = useDispatch()

	return (
		<Tooltip title={`Статус запуска 2 этапа: ${String(text)}`}>
			{text === true ?

				<FontAwesomeIcon
					style={{ cursor: 'pointer' }}
					onClick={(e) => {
						e.stopPropagation()

						dispatch({
							type: ActionTypes.MODEL_EDIT_LAUNCH_SECOND_STAGE,
							payload: {
								id: record.modelComponentId,
								launchSecondStage: false
							}
						})
					}} icon={faAnglesRight} />

				:
				<span
					style={{ cursor: 'pointer' }}
					onClick={(e) => {
						e.stopPropagation()

						dispatch({
							type: ActionTypes.MODEL_EDIT_LAUNCH_SECOND_STAGE,
							payload: {
								id: record.modelComponentId,
								launchSecondStage: true
							}
						})
					}}>x</span>
			}
		</Tooltip>
	)
}