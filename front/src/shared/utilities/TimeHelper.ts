export class TimeHelper {
    
    static DateToTime = (data:string | Date | null | any):string => {
        const newDate = new Date(data || new Date()) 
        const hours = newDate.getHours() < 10 ? "0" + String(newDate.getHours()) : String(newDate.getHours())
        const minutes= newDate.getHours() < 10 ? "0" + String(newDate.getMinutes()) : String(newDate.getMinutes())
            return hours + ":" + minutes
    }

    static TimeToDate = (time:string):Date => {
        const date = new Date()
        const timeSplit = time.split(":")
        date.setHours(Number(timeSplit[0]))
        date.setMinutes(Number(timeSplit[1]))
        return date
    }
}
