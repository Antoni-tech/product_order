import { Link } from "react-router-dom";

import { CaretUpOutlined } from '@ant-design/icons';
import { Tooltip, Badge } from 'antd'
import type { TableColumnsType } from 'antd';

import { Connector } from "../../../../../shared/entities/Connector/Connector";
import { URLPaths } from "../../../../../config/application/URLPaths";
import { Rule } from "../../../../../shared/entities/Rule/Rule";

import { FontAwesomeIcon } from "@fortawesome/react-fontawesome";
import {
	faCircleChevronDown,
	faCircleChevronUp,
	faPlay,
	faJ,
	faFileWord,
	faUsers,
	faBrain,
	faFont,
	faQ,
	faStop,
	faPause, faLock, faSheetPlastic, faCode,
	faCodePullRequest, faSpinner, faFileCircleCheck, faFileLines, faEllipsis, faFileCode
} from "@fortawesome/free-solid-svg-icons";
import modelRun from '/static/modelRun.svg'

import { SingleDataStructure } from "../../../../../shared/entities/DataStructure/DataStructure";

const imageUrl = process.env.PUBLIC_URL + '/static/modelRun.svg';
const defaultRule = process.env.PUBLIC_URL + '/static/defaultRule.svg';
const inputCon = process.env.PUBLIC_URL + '/static/input_con.svg'

const sharedIcon = process.env.PUBLIC_URL + '/static/common.svg'
const sendCon = process.env.PUBLIC_URL + '/static/sendCon.svg'

export const modelListCols: TableColumnsType<Connector> = [
	{
		title: '',
		dataIndex: 'state',
		key: 'state',

		render: (text, record) =>
			<>
				{text === 'RUN' ?
					<span style={{ width: '100%', display: 'flex', justifyContent: 'center' }}>
						<img src={imageUrl} alt="running" style={{ width: 20, paddingLeft: '0px' }} />
					</span>
					:
					<FontAwesomeIcon icon={faStop} />
				}
			</>

		,
		width: '5%',
		ellipsis: true,
		align: 'center'
	},
	{
		title: '',
		dataIndex: 'summaryState',
		key: 'summaryState',
		render: (text, record) =>
			<span>
				{text === 'DRAFT' ? <FontAwesomeIcon icon={faSheetPlastic} /> : <FontAwesomeIcon icon={faFileCircleCheck} />}
			</span>
		,
		width: '5%',
		ellipsis: true,
		align: 'center'
	},
	{
		title: 'Name',
		dataIndex: 'name',
		key: 'name',
		render: (text, record) =>
			<Tooltip title={`${text}`}>
				<Link to={`${URLPaths.MODEL_EDIT.link}/${record.versionId}`}>{text}</Link>
			</Tooltip>,
		width: '40%',
		ellipsis: true,
	},

	{
		title: 'Создал',
		dataIndex: 'userName',
		key: 'userName',
		ellipsis: true,
		width: '15%',
		render: (text, record) =>
			<Tooltip title={record.versionId % 3 === 0 ? `System` : `${text}`}>
				{record.versionId % 3 === 0 ? <span><FontAwesomeIcon icon={faLock} /> System</span> : <span>{text}</span>}
			</Tooltip>,
	},

	{
		title: 'Полных обработок',
		dataIndex: 'amountOfTransactions',
		key: 'amountOfTransactions',
		ellipsis: true,
		align: 'center',
		width: '10%',
		render(value, record, index) {
			return <span style={{ display: 'inline-block', minWidth: 50, borderRadius: 8, backgroundColor: '#21AAFF', color: '#fff', padding: '0 5px' }}>{value}</span>
		},
	},
	{
		title: 'Ошибок',
		dataIndex: 'amountOfErrors',
		key: 'amountOfErrors',
		ellipsis: true,
		width: '10%',
		align: 'center',
		render(text, record, index) {

			const randomNumber = Math.floor(Math.random() * 9) + 2;


			return <span style={{ display: 'inline-block', borderRadius: 8, backgroundColor: '#FF4D4D', color: '#fff', padding: '0 5px', width: '50px' }}>{text}</span>
		},
	},
	{
		title: 'Время последней операции',
		dataIndex: 'createDate',
		key: 'createDate',
		ellipsis: true,
		width: '15%',
		render: (text) => {
			const date = new Date(text);
			const formattedDate = `${date.getDate() < 10 ? '0' : ''}${date.getDate()}.${(date.getMonth() + 1) < 10 ? '0' : ''}${date.getMonth() + 1}.${date.getFullYear()} ${date.getHours()}:${date.getMinutes()}:${date.getSeconds()}`;
			return <span>{formattedDate}</span>
		}
	},
]

export const ConnectorListCols: TableColumnsType<Connector> = [

	{
		dataIndex: 'type',
		key: 'type',
		align: 'center',
		width: '4%',
		render: (text, record) =>
			<Tooltip title={`${text}`}>
				{/* <FontAwesomeIcon icon={text === 'CONNECTOR_OUTPUT' ? faCircleChevronUp : faCircleChevronDown} /> */}
				{text === 'CONNECTOR_INPUT' ?
					<span style={{ width: '100%', display: 'flex', justifyContent: 'center' }}>
						<img src={inputCon} alt="running" style={{ width: 20, paddingLeft: '0px' }} />
					</span>
					:
					<span style={{ width: '100%', display: 'flex', justifyContent: 'center', }}>
						<img src={inputCon} alt="running" style={{ width: 20, paddingLeft: '0px', transform: 'rotate(180deg)' }} />
					</span>

				}
			</Tooltip>
	},
	{
		title: 'Name',
		dataIndex: 'name',
		key: 'name',
		ellipsis: true,
		width: '25%',
		render: (text, record) =>
			<Tooltip title={`${text}`}>
				<Link to=
					{record.type === 'CONNECTOR_INPUT' ?
						`${URLPaths.IN_CONNECTOR_EDIT.link}/${record.versionId}` :
						`${URLPaths.OUT_CONNECTOR_EDIT.link}/${record.versionId}`
					}>{text}</Link>
			</Tooltip>

	},
	{
		title: 'Создал',
		dataIndex: 'userName',
		key: 'userName',
		ellipsis: true,
		width: '10%',
		render: (text) =>
			<Tooltip title={`${text}`}>
				<span>{text}</span>
			</Tooltip>,
	},
	{
		dataIndex: 'summarySubType',
		key: 'summarySubType',
		ellipsis: true,
		width: '4%',
		align: 'center',
		render: (text, record) =>
			<Tooltip title={`${text}`}>
				{text === 'REQUESTER' && record.type === 'CONNECTOR_OUTPUT' ?
					<span style={{ width: '100%', display: 'flex', justifyContent: 'center', }}>
						<img src={sendCon} alt="running" style={{ width: 20, paddingLeft: '0px' }} />
					</span>
					:
					<FontAwesomeIcon icon={text === 'REQUESTER' ? faCodePullRequest : faSpinner} />}
			</Tooltip>,
	},
	{
		dataIndex: 'dataFormat',
		key: 'dataFormat',
		width: '4%',
		align: 'center',
		render: (text, record) =>
			<Tooltip title={text ? text : 'etc'}>
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

		dataIndex: 'common',
		key: 'common',
		width: '4%',
		align: 'center',
		render: (text, record) =>
			<Tooltip title={text}>
				<span style={{ width: '100%', height: '100%', display: 'flex', justifyContent: 'center', alignItems: 'flex-end' }}>
					<img src={sharedIcon} alt="shared" style={{ width: 20, paddingLeft: '0px' }} />
				</span>
			</Tooltip>
	},
	{
		title: 'Используется в моделях',
		dataIndex: 'models',
		key: 'models',
		ellipsis: true,
		width: '35%',
		render: (models: { modelName: string }[]) => (
			<span>
				{models.map((model, index, array) => (
					<span key={index}>
						{model.modelName}
						{index === array.length - 1 ? '.' : ', '}
					</span>
				))}
			</span>
		),
	},

	{
		title: 'Время последней операции',
		dataIndex: 'createDate',
		key: 'createDate',
		ellipsis: true,
		width: '14%',
		render: (text) => {
			const date = new Date(text);
			const formattedDate = `${date.getDate() < 10 ? '0' : ''}${date.getDate()}.${(date.getMonth() + 1) < 10 ? '0' : ''}${date.getMonth() + 1}.${date.getFullYear()} ${date.getHours()}:${date.getMinutes()}:${date.getSeconds()}`;
			return <span>{formattedDate}</span>
		}
	},


	// {
	// 	title: 'Type',
	// 	dataIndex: 'type',
	// 	key: 'type',
	// 	width: 0,
	// 	render: (text, record) => <span>{text === 'RULE' && 'ruleType' in record ? (record as Rule).ruleType : text}</span>
	// },

]

export const RuleListCols: TableColumnsType<Rule> = [


	{
		title: 'Name',
		dataIndex: 'name',
		key: 'name',
		width: '25%',
		render: (text, record) => <Link to={`${URLPaths.RULES_EDIT.link}/${record.versionId}`}>{text}</Link>
	},
	{
		title: 'Создал',
		dataIndex: 'userName',
		key: 'userName',
		width: '10%',
		render: (text, record) => <span>{text}</span>
	},
	{
		title: 'Цель',
		dataIndex: 'purpose',
		key: 'purpose',
		width: '30%',
		ellipsis: true,
		render: (text, record) => <span>Определение инцидента</span>
	},
	{
		dataIndex: 'type',
		key: 'type',
		width: '4%',
		align: 'center',
		render: (text, record) => text === 'RULE' ?
			<span style={{ width: '100%', height: '100%', display: 'flex', alignItems: 'flex-end', justifyContent: 'center', }}>
				<img src={defaultRule} alt="running" style={{ width: 20, paddingLeft: '0px', marginBottom: '-4px' }} />
			</span>
			: <FontAwesomeIcon icon={faBrain} />


	},
	{
		dataIndex: 'summarySubType',
		key: 'summarySubType',
		width: '4%',
		align: 'center',
		render: (text, record) =>
			<Tooltip title={text}>
				{text === 'QUALITY' ?
					<FontAwesomeIcon icon={faQ} /> :
					<Badge count={<CaretUpOutlined />} size='small'>
						<FontAwesomeIcon icon={faQ} />
					</Badge>}
			</Tooltip>
	},
	{
		dataIndex: 'common',
		key: 'common',
		width: '4%',
		align: 'center',
		render: (text) => <span style={{ width: '100%', height: '100%', display: 'flex', justifyContent: 'center', alignItems: 'flex-end' }}>
			<img src={sharedIcon} alt="shared" style={{ width: 20, paddingLeft: '0px' }} />
		</span>
	},
	{
		dataIndex: 'summaryState',
		key: 'summaryState',
		width: '4%',
		align: 'center',
		render: (text, record) => <FontAwesomeIcon icon={text === 'DRAFT' ? faSheetPlastic : faFileCircleCheck} />
	},
	{
		title: 'Время последней операции',
		dataIndex: 'last',
		key: 'last',
		ellipsis: true,
		width: '15%',
	},


	// {
	// 	title: 'Type',
	// 	dataIndex: 'type',
	// 	key: 'type',
	// 	width: 0,
	// 	render: (text, record) => <span>{text === 'RULE' && 'ruleType' in record ? (record as Rule).ruleType : text}</span>
	// },

]

export const DataStructureListCols: TableColumnsType<SingleDataStructure> = [
	{
		title: 'Name',
		dataIndex: 'name',
		key: 'name',
		width: '50%',
		ellipsis: true,
		render: (text, record) => <Link to={`${URLPaths.DATASTRUCTURE_EDIT.link}/${record.versionId}`}>{text}</Link>

	},
	{
		title: 'Создал',
		dataIndex: 'userName',
		key: 'userName',
		ellipsis: true,
		width: '10%',
	},
	{
		title: 'Модели',
		dataIndex: 'models',
		key: 'models',
		ellipsis: true,
		width: '40%',
		render: (models: { name: string }[]) =>
			<span>
				{
					models.map((model, index) => {
						return <span key={index}>{model.name},</span>
					})
				}</span>
	},
	// {
	// 	title: 'Коннекторы',
	// 	dataIndex: 'connectors',
	// 	key: 'connectors',
	// 	ellipsis: true,
	// 	width: '25%',
	// 	render: (connectors: { name: string }[]) =>
	// 		<span>
	// 			{
	// 				connectors.map((connector, index) => {
	// 					return <span key={index}>{connector.name},</span>
	// 				})
	// 			}</span>
	// },
	// {
	// 	title: 'В составе группы',
	// 	dataIndex: 'inStructure',
	// 	key: 'inStructure',
	// 	ellipsis: true,
	// 	width: '20%',
	// }
]