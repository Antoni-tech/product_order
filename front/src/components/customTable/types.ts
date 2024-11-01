
export interface DataType {
	id: number;
	key: number;
	name: string;
	fieldType: string;
	maxSize: number;
	allowEmpty: boolean;
	prohibitSpecCharacters: boolean;
	allowArray: boolean;
	maxArray: number;

	type?: string;
	newItem?: boolean;
}

