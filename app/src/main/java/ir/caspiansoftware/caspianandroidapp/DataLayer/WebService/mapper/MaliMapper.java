package ir.caspiansoftware.caspianandroidapp.DataLayer.WebService.mapper;

import static java.util.stream.Collectors.toList;

import java.util.List;

import ir.caspiansoftware.caspianandroidapp.DataLayer.WebService.dto.MaliDto;
import ir.caspiansoftware.caspianandroidapp.Models.MaliModel;

public class MaliMapper {

    public static MaliDto toMaliDto(MaliModel maliModel) {
        MaliDto dto = new MaliDto();
        dto.setMaliId(maliModel.getId());
        dto.setMaliType(maliModel.getMaliType());
        dto.setCodeBed(maliModel.getPersonBedModel() != null ? maliModel.getPersonBedModel().getCode() : null);
        dto.setCodeBes(maliModel.getPersonBesModel() != null ? maliModel.getPersonBesModel().getCode() : null);
        dto.setMaliDate(maliModel.getMaliDate());
        dto.setDescription(maliModel.getDescription());
        dto.setVcheckSarresidDate(maliModel.getVcheckSarresidDate());
        dto.setVcheckBank(maliModel.getVcheckBank());
        dto.setVcheckSerial(maliModel.getVcheckSerial());
        dto.setAmount(maliModel.getAmount());
        dto.setLat(maliModel.getLat());
        dto.setLon(maliModel.getLon());
        dto.setCreateDate(maliModel.getCreateDate());
        return dto;
    }

    public static List<MaliDto> toMaliDto(List<MaliModel> maliModel) {
        return maliModel.stream().map(MaliMapper::toMaliDto).collect(toList());
    }
}
