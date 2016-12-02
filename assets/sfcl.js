/**
 * 
 */
function Calculate(sum, lb,  sfcl) {
	switch (sfcl) {
	case "B":
		sum = sum + switchB(lb);
		break;
	case "C":
		sum = sum + switchC(lb);
		break;
	case "G":
		sum = sum + switchG(lb);
		break;
	}
	return sum;
}

function switchB(lb) {
	if (lb=="普通中小学生") {
		return 180;
		if (lb=="普通大学生")
			return 180;
		if (lb=="普通城乡居民")
			return 180;
		if (lb=="处于劳动年龄段非从业居民")
			return 180;
		return 0;

	}

}
function switchC(lb) {
	if (lb=="普通中小学生")
		return 150;
	if (lb=="普通大学生")
		return 150;
	if (lb=="普通城乡居民")
		return 150;
	if (lb=="低收入家庭60岁以上老年人")
		return 60;
	return 0;

}
function switchG(lb) {
	return 150;
}

